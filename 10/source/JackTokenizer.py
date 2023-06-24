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
        self.current_token = None
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

    def get_tokens_from_file(self):
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
        tfile_name = self.output_file_name.replace(".xml", "T.xml")
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

                self.tokens_found.append(token_value)

                f.write(str_out_line)
                f.write("\n")

            f.write("</tokens>\n")

        return self.tokens_found

    def get_tokens_count(self):
        return len(self.tokens_found)
    
    def advance(self):
        return self.tokens_found.pop(0)

