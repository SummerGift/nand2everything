import re

# Define the token types and their regular expressions
TOKENS = [
    ("KEYWORD", r"\b(if|else|while|for)\b"),
    ("IDENTIFIER", r"\b[a-zA-Z_][a-zA-Z0-9_]*\b"),
    ("NUMBER", r"\b\d+\b"),
    ("OPERATOR", r"[+\-*/]"),
    ("WHITESPACE", r"\s+"),
]

def tokenize(text):
    tokens = []
    position = 0

    while position < len(text):
        # Try to match each token type at the current position
        for token_type, pattern in TOKENS:
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

    return tokens

# Example usage
text = "if x + 42 while"
tokens = tokenize(text)
print(tokens)