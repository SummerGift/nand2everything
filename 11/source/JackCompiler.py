import os
import glob
import sys
from JackTokenizer import JackTokenizer
from CompilationEngine import CompilationEngine

class JackCompiler:
    @staticmethod
    def run(input_file_name, output_file):
        tokenizer = JackTokenizer(input_file_name, output_file)
        compiler = CompilationEngine(tokenizer, output_file)
        compiler.compile_class()

    @staticmethod
    def output_file_for(input_file):
        file_name, _ = os.path.splitext(os.path.basename(input_file))
        dir_name = os.path.dirname(input_file).replace('./', '')
        return os.path.join(dir_name, file_name + ".vm")

def get_files_from_path(path):
    return [path] if os.path.isfile(path) else glob.glob(os.path.join(path, "*.jack"))

def process_file(file):
    output_file_name = JackCompiler.output_file_for(file)
    JackCompiler.run(file, output_file_name)

def main():
    if len(sys.argv) != 2:
        print("Warning: Please provide the source folder path.")
        return

    source_path = sys.argv[1]
    files = get_files_from_path(source_path)

    for file in files:
        process_file(file)

if __name__ == '__main__':
    main()

