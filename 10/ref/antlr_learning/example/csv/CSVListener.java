// Generated from CSV.g4 by ANTLR 4.9
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CSVParser}.
 */
public interface CSVListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CSVParser#file}.
	 * @param ctx the parse tree
	 */
	void enterFile(CSVParser.FileContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSVParser#file}.
	 * @param ctx the parse tree
	 */
	void exitFile(CSVParser.FileContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSVParser#hdr}.
	 * @param ctx the parse tree
	 */
	void enterHdr(CSVParser.HdrContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSVParser#hdr}.
	 * @param ctx the parse tree
	 */
	void exitHdr(CSVParser.HdrContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSVParser#row}.
	 * @param ctx the parse tree
	 */
	void enterRow(CSVParser.RowContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSVParser#row}.
	 * @param ctx the parse tree
	 */
	void exitRow(CSVParser.RowContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSVParser#field}.
	 * @param ctx the parse tree
	 */
	void enterField(CSVParser.FieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSVParser#field}.
	 * @param ctx the parse tree
	 */
	void exitField(CSVParser.FieldContext ctx);
}