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


def asm_line_translate(command_line):
    command_line = command_line[:-1]

    binary_str = ""

    # handle command translate according to the first character in command
    if command_line[0] == "@":
        # handle A command
        binary_str += "0"
        binary_number = bin(int(command_line[1:]))[2:]

        binary_len = len(binary_number)
        if binary_len < 15:
            binary_number = (15 - binary_len) * "0" + binary_number

        # construct A command with 0 and 15-bit address
        binary_str = binary_str + binary_number

        return binary_str
    else:
        # handle C command
        binary_str += "111"

        comp_str = ""
        dest_str = ""
        jump_str = ""

        # handle comp_str
        if command_line.find("=") != -1:
            # no need to jump
            print("no need to jump")

        # handle jump_str
        if command_line.find(";") == -1:
            jump_str = "000"
        else:
            jump_str = "tbc"













    # for char in command_line:
    #     print(char)


def asm_file_translate(src_list):
    binary_list = []
    for line in src_list:
        binary_list.append(asm_line_translate(line))

    # print(binary_list)


def main():
    # if len(sys.argv) is not 2:
    #     print("Useage: python assembler xxx.asm.")
    #     return

    # get arg from command line
    # asm_file = sys.argv[1]
    asm_file = "/Users/mac/work/nand2everything/material/projects/06/add/Add.asm"

    src_list = handle_white_space(asm_file)
    asm_file_translate(src_list)

    return True


if __name__ == "__main__":
    main()
