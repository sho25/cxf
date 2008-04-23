begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// $ANTLR 2.7.4: "idl.g" -> "IDLParser.java"$
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|corba
operator|.
name|processors
operator|.
name|idl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Vector
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Hashtable
import|;
end_import

begin_interface
specifier|public
interface|interface
name|IDLTokenTypes
block|{
name|int
name|EOF
init|=
literal|1
decl_stmt|;
name|int
name|NULL_TREE_LOOKAHEAD
init|=
literal|3
decl_stmt|;
name|int
name|SEMI
init|=
literal|4
decl_stmt|;
name|int
name|LITERAL_abstract
init|=
literal|5
decl_stmt|;
name|int
name|LITERAL_local
init|=
literal|6
decl_stmt|;
name|int
name|LITERAL_interface
init|=
literal|7
decl_stmt|;
name|int
name|LITERAL_custom
init|=
literal|8
decl_stmt|;
name|int
name|LITERAL_valuetype
init|=
literal|9
decl_stmt|;
name|int
name|LITERAL_eventtype
init|=
literal|10
decl_stmt|;
name|int
name|LITERAL_module
init|=
literal|11
decl_stmt|;
name|int
name|LCURLY
init|=
literal|12
decl_stmt|;
name|int
name|RCURLY
init|=
literal|13
decl_stmt|;
name|int
name|COLON
init|=
literal|14
decl_stmt|;
name|int
name|COMMA
init|=
literal|15
decl_stmt|;
name|int
name|SCOPEOP
init|=
literal|16
decl_stmt|;
name|int
name|IDENT
init|=
literal|17
decl_stmt|;
name|int
name|LITERAL_truncatable
init|=
literal|18
decl_stmt|;
name|int
name|LITERAL_supports
init|=
literal|19
decl_stmt|;
name|int
name|LITERAL_public
init|=
literal|20
decl_stmt|;
name|int
name|LITERAL_private
init|=
literal|21
decl_stmt|;
name|int
name|LITERAL_factory
init|=
literal|22
decl_stmt|;
name|int
name|LPAREN
init|=
literal|23
decl_stmt|;
name|int
name|RPAREN
init|=
literal|24
decl_stmt|;
name|int
name|LITERAL_in
init|=
literal|25
decl_stmt|;
name|int
name|LITERAL_const
init|=
literal|26
decl_stmt|;
name|int
name|ASSIGN
init|=
literal|27
decl_stmt|;
name|int
name|OR
init|=
literal|28
decl_stmt|;
name|int
name|XOR
init|=
literal|29
decl_stmt|;
name|int
name|AND
init|=
literal|30
decl_stmt|;
name|int
name|LSHIFT
init|=
literal|31
decl_stmt|;
name|int
name|RSHIFT
init|=
literal|32
decl_stmt|;
name|int
name|PLUS
init|=
literal|33
decl_stmt|;
name|int
name|MINUS
init|=
literal|34
decl_stmt|;
name|int
name|STAR
init|=
literal|35
decl_stmt|;
name|int
name|DIV
init|=
literal|36
decl_stmt|;
name|int
name|MOD
init|=
literal|37
decl_stmt|;
name|int
name|TILDE
init|=
literal|38
decl_stmt|;
name|int
name|LITERAL_TRUE
init|=
literal|39
decl_stmt|;
name|int
name|LITERAL_FALSE
init|=
literal|40
decl_stmt|;
name|int
name|LITERAL_typedef
init|=
literal|41
decl_stmt|;
name|int
name|LITERAL_native
init|=
literal|42
decl_stmt|;
name|int
name|LITERAL_float
init|=
literal|43
decl_stmt|;
name|int
name|LITERAL_double
init|=
literal|44
decl_stmt|;
name|int
name|LITERAL_long
init|=
literal|45
decl_stmt|;
name|int
name|LITERAL_short
init|=
literal|46
decl_stmt|;
name|int
name|LITERAL_unsigned
init|=
literal|47
decl_stmt|;
name|int
name|LITERAL_char
init|=
literal|48
decl_stmt|;
name|int
name|LITERAL_wchar
init|=
literal|49
decl_stmt|;
name|int
name|LITERAL_boolean
init|=
literal|50
decl_stmt|;
name|int
name|LITERAL_octet
init|=
literal|51
decl_stmt|;
name|int
name|LITERAL_any
init|=
literal|52
decl_stmt|;
name|int
name|LITERAL_Object
init|=
literal|53
decl_stmt|;
name|int
name|LITERAL_struct
init|=
literal|54
decl_stmt|;
name|int
name|LITERAL_union
init|=
literal|55
decl_stmt|;
name|int
name|LITERAL_switch
init|=
literal|56
decl_stmt|;
name|int
name|LITERAL_case
init|=
literal|57
decl_stmt|;
name|int
name|LITERAL_default
init|=
literal|58
decl_stmt|;
name|int
name|LITERAL_enum
init|=
literal|59
decl_stmt|;
name|int
name|LITERAL_sequence
init|=
literal|60
decl_stmt|;
name|int
name|LT
init|=
literal|61
decl_stmt|;
name|int
name|GT
init|=
literal|62
decl_stmt|;
name|int
name|LITERAL_string
init|=
literal|63
decl_stmt|;
name|int
name|LITERAL_wstring
init|=
literal|64
decl_stmt|;
name|int
name|LBRACK
init|=
literal|65
decl_stmt|;
name|int
name|RBRACK
init|=
literal|66
decl_stmt|;
name|int
name|LITERAL_exception
init|=
literal|67
decl_stmt|;
name|int
name|LITERAL_oneway
init|=
literal|68
decl_stmt|;
name|int
name|LITERAL_void
init|=
literal|69
decl_stmt|;
name|int
name|LITERAL_out
init|=
literal|70
decl_stmt|;
name|int
name|LITERAL_inout
init|=
literal|71
decl_stmt|;
name|int
name|LITERAL_raises
init|=
literal|72
decl_stmt|;
name|int
name|LITERAL_context
init|=
literal|73
decl_stmt|;
name|int
name|LITERAL_fixed
init|=
literal|74
decl_stmt|;
name|int
name|LITERAL_ValueBase
init|=
literal|75
decl_stmt|;
name|int
name|LITERAL_import
init|=
literal|76
decl_stmt|;
name|int
name|LITERAL_typeid
init|=
literal|77
decl_stmt|;
name|int
name|LITERAL_typeprefix
init|=
literal|78
decl_stmt|;
name|int
name|LITERAL_readonly
init|=
literal|79
decl_stmt|;
name|int
name|LITERAL_attribute
init|=
literal|80
decl_stmt|;
name|int
name|LITERAL_getraises
init|=
literal|81
decl_stmt|;
name|int
name|LITERAL_setraises
init|=
literal|82
decl_stmt|;
name|int
name|LITERAL_component
init|=
literal|83
decl_stmt|;
name|int
name|LITERAL_provides
init|=
literal|84
decl_stmt|;
name|int
name|LITERAL_uses
init|=
literal|85
decl_stmt|;
name|int
name|LITERAL_multiple
init|=
literal|86
decl_stmt|;
name|int
name|LITERAL_emits
init|=
literal|87
decl_stmt|;
name|int
name|LITERAL_publishes
init|=
literal|88
decl_stmt|;
name|int
name|LITERAL_consumes
init|=
literal|89
decl_stmt|;
name|int
name|LITERAL_home
init|=
literal|90
decl_stmt|;
name|int
name|LITERAL_manages
init|=
literal|91
decl_stmt|;
name|int
name|LITERAL_primarykey
init|=
literal|92
decl_stmt|;
name|int
name|LITERAL_finder
init|=
literal|93
decl_stmt|;
name|int
name|INT
init|=
literal|94
decl_stmt|;
name|int
name|OCTAL
init|=
literal|95
decl_stmt|;
name|int
name|HEX
init|=
literal|96
decl_stmt|;
name|int
name|STRING_LITERAL
init|=
literal|97
decl_stmt|;
name|int
name|WIDE_STRING_LITERAL
init|=
literal|98
decl_stmt|;
name|int
name|CHAR_LITERAL
init|=
literal|99
decl_stmt|;
name|int
name|WIDE_CHAR_LITERAL
init|=
literal|100
decl_stmt|;
name|int
name|FIXED
init|=
literal|101
decl_stmt|;
name|int
name|FLOAT
init|=
literal|102
decl_stmt|;
name|int
name|QUESTION
init|=
literal|103
decl_stmt|;
name|int
name|DOT
init|=
literal|104
decl_stmt|;
name|int
name|NOT
init|=
literal|105
decl_stmt|;
name|int
name|WS
init|=
literal|106
decl_stmt|;
name|int
name|PREPROC_DIRECTIVE
init|=
literal|107
decl_stmt|;
name|int
name|SL_COMMENT
init|=
literal|108
decl_stmt|;
name|int
name|ML_COMMENT
init|=
literal|109
decl_stmt|;
name|int
name|ESC
init|=
literal|110
decl_stmt|;
name|int
name|VOCAB
init|=
literal|111
decl_stmt|;
name|int
name|DIGIT
init|=
literal|112
decl_stmt|;
name|int
name|NONZERODIGIT
init|=
literal|113
decl_stmt|;
name|int
name|OCTDIGIT
init|=
literal|114
decl_stmt|;
name|int
name|HEXDIGIT
init|=
literal|115
decl_stmt|;
name|int
name|ESCAPED_IDENT
init|=
literal|116
decl_stmt|;
block|}
end_interface

end_unit

