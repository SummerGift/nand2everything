from JackTokenizer import JackTokenizer
from CompilationEngine import CompilationEngine
import sys
import os
import glob

class JackCompiler():
    @classmethod
    def run(cls, input_file_name, output_file):

        print("input_file_name:" + input_file_name)
        print("output_file_name:" + output_file)

        tokenizer = JackTokenizer(input_file_name, output_file)
        compiler = CompilationEngine(tokenizer, output_file)
        compiler.compile_class()

    @classmethod
    def output_file_for(cls, input_file):
        file_name = os.path.basename(input_file).split(".")[0]
        ext_name = ".vm"
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
        output_file_name = JackCompiler.output_file_for(input_file_name)
        JackCompiler.run(input_file_name, output_file_name)


if __name__ == '__main__':
    main()
