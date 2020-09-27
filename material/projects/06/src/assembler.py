import os
import sys


def handle_white_space(file):
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

    file_dirname = os.path.dirname(file)
    filename = os.path.basename(file)
    new_filename = filename + ".nowhitespace"
    output_file = os.path.join(file_dirname, new_filename)

    with open(output_file, 'w') as f:
        for line in new_list:
            f.write(line)

    return new_list


def main():

    if len(sys.argv) is not 2:
        print("Useage: python assembler xxx.asm.")
        return

    # get arg from command line
    asm_file = sys.argv[1]
    handle_white_space(asm_file)

    return


if __name__ == "__main__":
    main()
