from JackTokenizer import JackTokenizer
import sys
import os
import glob
import shutil


class JackAnalyzer():
    @classmethod
    def run(cls, input_file_name, output_file_name):
        tokenizer = JackTokenizer(input_file_name, output_file_name)
        token_need = tokenizer.tokenize()

    @classmethod
    def xml_output_file_for(cls, input_file):
        file_name = os.path.basename(input_file).split(".")[0]
        ext_name = ".xml"
        dir_name = os.path.dirname(input_file).replace('./', '')

        # output file overwriting existing file..
        return "{}/{}{}".format(dir_name, file_name, ext_name)


def main():
    if len(sys.argv) != 2:
        print("Warning:Please type in your source folder path.")
        return

    pathname = sys.argv[1]

    # determine output file names
    if os.path.isfile(pathname):
        files = [pathname]
    elif os.path.isdir(pathname):
        jack_path = os.path.join(pathname, "*.jack")
        files = glob.glob(jack_path)

    for input_file_name in files:
        output_file_name = JackAnalyzer.xml_output_file_for(input_file_name)

        print("input_file_name:" + input_file_name)
        JackAnalyzer.run(input_file_name, output_file_name)


if __name__ == '__main__':
    main()
