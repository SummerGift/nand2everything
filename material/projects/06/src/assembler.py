import os


class Parser:
    """Parser for assembler."""
    src_list = 0

    def __init__(self, file_pathname):
        self.pathname = file_pathname
        self.command_line_list = []

    def handle_white_space(self):
        with open(self.pathname) as f:
            list_data = list(f)

        for line in list_data:
            if line.startswith(" "):
                continue
            if line.startswith("//"):
                continue
            if line.startswith("\n"):
                continue

            self.command_line_list.append(line)

    @staticmethod
    def get_command_type(single_cmd):
        if single_cmd[0] == "@":
            return "COMMAND_A"
        else:
            return "COMMAND_C"

    @staticmethod
    def handle_a_command(command_line):
        """handle A command
        :param command_line: a single command to translate
        :return: binary command
        """
        prefix = "0"
        binary_addr = bin(int(command_line[1:]))[2:]

        binary_len = len(binary_addr)
        if binary_len < 15:
            binary_addr = (15 - binary_len) * "0" + binary_addr

        # construct A command with 0 and 15-bit address
        binary_str = prefix + binary_addr
        return binary_str

    @staticmethod
    def comp_str(self, command_line):
        if command_line.find("=") != -1:
            command = command_line[command_line.find("=") + 1:]

        if command_line.find(";") != -1:
            command = command_line[:command_line.find(";")]

        print("comp str:", command)

        dest_command_dict = {
            "0": "0101010",
            "1": "0111111",
            "-1": "0111010",
            "D": "0001100",
            "A": "0110000",
            "!D": "0001101",
            "!A": "0110001",
            "-D": "0001111",
            "-A": "0110011",
            "D+1": "0011111",
            "A+1": "0110111",
            "D-1": "0001110",
            "A-1": "0110010",
            "D+A": "0000010",
            "D-A": "0010011",
            "A-D": "0000111",
            "D&A": "0000000",
            "D|A": "0010101",

            "M": "1110000",
            "!M": "1110001",
            "-M": "1110011",
            "M+1": "1110111",
            "M-1": "1110010",
            "D+M": "1000010",
            "D-M": "1010011",
            "M-D": "1000111",
            "D&M": "1000000",
            "D|M": "1010101",
        }

        return dest_command_dict[command]

    @staticmethod
    def dest_str(self, command_line):
        if command_line.find("=") == -1:
            return "000"
        else:
            command = command_line[:command_line.find("=")]
            print("dest command", command)

            dest_command_dict = {
                "M": "001",
                "D": "010",
                "MD": "011",
                "A": "100",
                "AM": "101",
                "AD": "110",
                "ADM": "111",
            }

            return dest_command_dict[command]

    @staticmethod
    def jump_str(self, command_line):
        if command_line.find(";") == -1:
            return "000"
        else:
            command = command_line[command_line.find(";") + 1:]
            print("jump COMMAND: ", command)

            jump_command_dict = {
                "JGT": "001",
                "JEQ": "010",
                "JGE": "011",
                "JLT": "100",
                "JNE": "101",
                "JLE": "110",
                "JMP": "111",
            }

            return jump_command_dict[command]

    def handle_c_command(self, command_line):
        """handle C command"""
        prefix = "111"

        comp_str = self.comp_str(self, command_line)
        dest_str = self.dest_str(self, command_line)
        jump_str = self.jump_str(self, command_line)

        binary_command = prefix + comp_str + dest_str + jump_str

        return binary_command

    def asm_line_translate(self, command_line):
        command_line = command_line[:-1]

        if self.get_command_type(command_line) == "COMMAND_A":
            return self.handle_a_command(command_line)

        if self.get_command_type(command_line) == "COMMAND_C":
            return self.handle_c_command(command_line)

    def asm_file_translate(self):
        self.handle_white_space()

        binary_list = []
        for line in self.command_line_list:
            binary_list.append(self.asm_line_translate(line))
        return binary_list


def main():
    # if len(sys.argv) is not 2:
    #     print("Useage: python assembler xxx.asm.")
    #     return

    # get arg from command line
    # asm_file = sys.argv[1]
    # asm_file = "/Users/mac/work/nand2everything/material/projects/06/add/Add.asm"
    # asm_file = "/Users/mac/work/nand2everything/material/projects/06/max/MaxL.asm"
    # asm_file = "/Users/mac/work/nand2everything/material/projects/06/pong/PongL.asm"
    asm_file = "/Users/mac/work/nand2everything/material/projects/06/rect/RectL.asm"

    parser = Parser(asm_file)
    binary_code = parser.asm_file_translate()

    file_dirname = os.path.dirname(asm_file)
    filename = os.path.basename(asm_file)
    new_filename = filename + "_summer.hack"
    output_file = os.path.join(file_dirname, new_filename)

    with open(output_file, 'w') as f:
        for line in binary_code:
            f.write(line + "\n")

    print(binary_code)

    return True


if __name__ == "__main__":
    main()
