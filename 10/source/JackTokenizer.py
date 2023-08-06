import re


class JackTokenizer:
    """
    goes through a .jack input file and produces a stream of tokens
    ignores all whitespace and comments
    """

    def __init__(self, input_file_name, output_file_name):
        self.input_file_name = input_file_name
        self.output_file_name = output_file_name
        self.tokens_found = []
        self.tokens_save = []
        self.current_token = None
        self.current_token_dict = None
        self.current_token_value = None
        self.current_token_type = None
        self.next_token_dict = None
        self.next_token = None
        self.has_more_tokens = True

        # Define the token types and their regular expressions
        self.token_define = [
            ("comments", r"(/\*([^*]|[\r\n]|(\*+([^*/]|[\r\n])))*\*+/)|(//.*)"),
            (
                "keyword",
                r"\b(class|constructor|function|method|field|static|var|int|char|boolean|void|true|false|null|this|let|do|if|else|while|return)\b",
            ),
            ("symbol", r"[\{\}\(\)\[\]\.\,\;\+\-\*\/\&\|\<\>\=\~]"),
            ("integerConstant", r"\b\d+\b"),
            ("stringConstant", r"\"[^\n]*\""),
            ("keyword", r"\b(if|else|while|for)\b"),
            ("identifier", r"\b[a-zA-Z_][a-zA-Z0-9_]*\b"),
            ("whitespace", r"\s+"),
        ]

        self.symbol_conversions = {
            "<": "&lt;",
            ">": "&gt;",
            '"': "&quot;",
            "&": "&amp;",
        }

        self.tokentypes_conversions = {
            "keyword": "KEYWORD",
            "symbol": "SYMBOL",
            "integerConstant": "INT_CONST",
            "stringConstant": "STRING_CONST",
            "identifier": "IDENTIFIER",
        }

    def setup(self):
        tokens = []
        tokens_need = []
        position = 0

        with open(self.input_file_name, "r") as f:
            text = f.read()

        while position < len(text):
            # Try to match each token type at the current position
            for token_type, pattern in self.token_define:
                match = re.match(pattern, text[position:])
                if match:
                    # Store the matched token and its type
                    tokens.append((token_type, match.group(0)))
                    position += len(match.group(0))
                    break
            else:
                # No match found, handle the error
                print(f"Unexpected character '{text[position]}' at position {position}")
                position += 1

        # put the tokens we need to a new list
        for token in tokens:
            if token[0] == "whitespace":
                continue

            if token[0] == "comments":
                continue

            tokens_need.append({token[0]: token[1]})

        # Set a tokenizer compare file name
        tfile_name = self.output_file_name.replace("jack", "T.xml")
        print("output token filename :", tfile_name)

        with open(tfile_name, "w+") as f:
            f.write("<tokens>\n")

            for token in tokens_need:
                if "symbol" in token:
                    if token["symbol"] in self.symbol_conversions:
                        token["symbol"] = self.symbol_conversions[token["symbol"]]

                if "stringConstant" in token:
                    token["stringConstant"] = token["stringConstant"].replace('"', "")

                token_keys = token.keys()
                for key in token_keys:
                    token_type = str(key)

                token_values = token.values()
                for value in token_values:
                    token_value = str(value)

                str_out_line = "<{0}> {1} </{0}>".format(token_type, token_value)

                self.tokens_found.append({token_type: token_value})

                f.write(str_out_line)
                f.write("\n")

            f.write("</tokens>\n")

        for token in self.tokens_found:
            self.tokens_save.append(token)

        return self.tokens_found

    def get_tokens_count(self):
        return len(self.tokens_found)

    def hasMoreTokens(self):
        if self.get_tokens_count() > 0:
            return True
        else:
            self.has_more_tokens = False
            return False

    def advance(self):
        self.current_token_dict = self.tokens_found.pop(0)

        token_keys = self.current_token_dict.keys()
        token_type = None

        for key in token_keys:
            token_type = key

        # get current token type and value
        self.current_token_type = self.tokentypes_conversions[token_type]
        self.current_token = self.current_token_dict[token_type]

        if len(self.tokens_found) == 0:
            return None

        # get next token type and value
        next_token_dict = self.tokens_found[0]

        for key in next_token_dict.values():
            self.next_token = key

        return self.current_token_dict
    
    def part_of_subroutine_call(self):
        if len(self.tokens_found) < 3:
            return False
        
        index = len(self.tokens_save) - len(self.tokens_found) - 3

        token_dict = self.tokens_save[index]

        token_keys = token_dict.keys()

        token_type = None

        for key in token_keys:
            token_type = key

        token = token_dict[token_type]

        if token == ".":
            return True
        else:
            return False
