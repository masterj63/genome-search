import hashlib
import time
import os

needle = "wax synthase"
genomes_path = "../genomas"

def process_file(file_path, acc):
    with open(file_path, "r") as fin:
        for haystack in fin:
            if needle in haystack:
                acc.append(haystack)

def buscar():
    acc = []
    for root, dirs, files in os.walk(genomes_path):
        for file in files:
            file_path = os.path.join(root, file)
            process_file(file_path, acc)
    return "".join(acc)

if __name__ == "__main__":
    start = time.time()
    output = buscar()
    end = time.time()

    m = hashlib.sha256()
    m.update(output.encode("utf-8"))
    output_hash = m.hexdigest()

    print(f"{(end - start):.3f} seconds elapsed")
    print(f"output hash is {output_hash}")
    if output_hash != "48aa9f97c5d4e0dd1dd275f4eaa941e282758b9a03e8c930c6850874284ebe5c":
        raise Exception()
