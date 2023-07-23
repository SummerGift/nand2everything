
class SymbolTable():
    """
    Creates the current symbpls for a given class or subrotine
    """
    def __init__(self):
        self.symbols = []

    def reset(self):
        """starts a new subroutine scope,"""
        self.symbols = []

    def define(self, name, symbol_type, kind):
        """defines a new identifier of the given name, type, and kind and assigns it a running index"""

        new_symbol = {
            "name": name,
            "type": symbol_type,
            "kind": kind,
            "index": self.var_count(kind)
        }
        self.symbols.append(new_symbol)

    def var_count(self, kind):
        """return the number of variables of the given kind already defined in the current scope"""
        return sum(symbol['kind'] == kind for symbol in self.symbols)
    
    def kind_of(self, name):
        """return the kind of the name identifer in the current scope(STATIC, FIDLE, ARG, VAR, NONE)"""
        return self.find_symbol_by_name(name).get("kind")
    
    def type_of(self, name):
        """return the type of the named identifier in  the current scope"""
        return self.find_symbol_by_name(name).get("type")
    
    def index_of(self, name):
        """return the type of the named identifier in  the current scope"""
        return self.find_symbol_by_name(name).get("index")
    
    def find_symbol_by_name(self, name):
        """traverse all symbols to fine identifer name"""
        for symbol in self.symbols:
            if symbol["name"] == name:
                return symbol
