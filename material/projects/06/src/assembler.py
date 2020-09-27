import os
import sys


def read_asm_file(file):
    with open(file) as f:
        list_data = list(f)

    new_list = []
    for line in list_data:
        if line.startswith(" "):
            continue
        if line.startswith("//"):
            continue
        if line.startswith("\n"):
            continue

        new_list.append(line)

    print(new_list)

    with open("without_whitespace.asm") as f:
        for line in new_list:
            f.write(line)


def main():

    if len(sys.argv) is not 2:
        print("Useage: python assembler xxx.asm.")
        return

    read_asm_file(sys.argv[1])

    return



if __name__ == "__main__":
    main()
