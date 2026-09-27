#!/usr/bin/env python3
"""
Downloads {bucket}/{projectId}/my-react-app/ from MinIO to a local folder,
then runs `npm install` in it.

Usage:
    python download_project.py <projectId> [outputDir]

Requires the MinIO Python SDK:
    pip install minio
"""

import sys
import shutil
import socket
import subprocess
from pathlib import Path

from minio import Minio
from minio.error import S3Error

# --- Config: adjust if your setup differs -------------------------------
# Use "minio:9000" instead of "localhost:9000" if this script runs inside
# the same docker-compose network as the minio service.
MINIO_ENDPOINT = "localhost:9000"
MINIO_ACCESS_KEY = "minioadmin"
MINIO_SECRET_KEY = "minioadmin123"
MINIO_SECURE = False  # True if MinIO is served over https

BUCKET = "project"
APP_SUBPATH = "my-react-app"
DEV_PORT = 5173  # Vite's default dev server port
# -------------------------------------------------------------------------


def is_port_in_use(port: int, host: str = "127.0.0.1") -> bool:
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as sock:
        sock.settimeout(0.5)
        return sock.connect_ex((host, port)) == 0


def download_project(project_id: str, output_dir: Path) -> None:
    client = Minio(
        MINIO_ENDPOINT,
        access_key=MINIO_ACCESS_KEY,
        secret_key=MINIO_SECRET_KEY,
        secure=MINIO_SECURE,
    )

    prefix = f"{project_id}/{APP_SUBPATH}/"

    try:
        objects = list(client.list_objects(BUCKET, prefix=prefix, recursive=True))
    except S3Error as e:
        print(f"Error: could not list objects at '{BUCKET}/{prefix}': {e}", file=sys.stderr)
        sys.exit(1)

    if not objects:
        print(
            f"Error: nothing found at '{BUCKET}/{prefix}'. "
            "Check the projectId and bucket path.",
            file=sys.stderr,
        )
        sys.exit(1)

    print(f"Found {len(objects)} files under '{BUCKET}/{prefix}'. Downloading to '{output_dir}' ...")

    for obj in objects:
        # Strip the "{projectId}/my-react-app/" prefix so files land directly
        # under output_dir, preserving subfolders like src/
        relative_path = obj.object_name[len(prefix):]
        if not relative_path:
            continue  # skip the "directory marker" object itself, if any

        local_path = output_dir / relative_path
        local_path.parent.mkdir(parents=True, exist_ok=True)

        client.fget_object(BUCKET, obj.object_name, str(local_path))

    print("Download complete.")


def run_npm_install(output_dir: Path) -> None:
    # On Windows, npm is a .cmd/.bat shim, not a .exe, so subprocess.run
    # can't find it by name alone without shell=True. shutil.which()
    # resolves the right file (npm.cmd on Windows, npm on mac/Linux).
    npm_cmd = shutil.which("npm")
    if npm_cmd is None:
        print(
            "Error: 'npm' not found on PATH. Install Node.js first: https://nodejs.org/",
            file=sys.stderr,
        )
        sys.exit(1)

    print(f"Running npm install in {output_dir} ...")
    result = subprocess.run([npm_cmd, "install"], cwd=output_dir)
    if result.returncode != 0:
        print("npm install failed.", file=sys.stderr)
        sys.exit(result.returncode)


def run_npm_dev(output_dir: Path) -> None:
    npm_cmd = shutil.which("npm")
    if npm_cmd is None:
        print(
            "Error: 'npm' not found on PATH. Install Node.js first: https://nodejs.org/",
            file=sys.stderr,
        )
        sys.exit(1)

    print(f"Starting dev server in {output_dir} (npm run dev) ...")
    # This blocks and streams output until you stop it with Ctrl+C.
    subprocess.run([npm_cmd, "run", "dev"], cwd=output_dir)


def main() -> None:
    if len(sys.argv) < 2:
        print("Usage: python download_project.py <projectId> [outputDir]", file=sys.stderr)
        sys.exit(1)

    project_id = sys.argv[1]
    output_dir = Path(sys.argv[2]) if len(sys.argv) > 2 else Path(f"./{project_id}")

    download_project(project_id, output_dir)

    if is_port_in_use(DEV_PORT):
        print(f"Port {DEV_PORT} is already in use — assuming the dev server is already running. Skipping npm install and npm run dev.")
        return

    run_npm_install(output_dir)
    print(f"Project ready at: {output_dir}")
    run_npm_dev(output_dir)


if __name__ == "__main__":
    main()