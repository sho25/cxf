begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|commons_text
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Class holding various entity data for HTML and XML - generally for use with  * the LookupTranslator.  * All Maps are generated using {@code java.util.Collections.unmodifiableMap()}.  *  * @since 1.0  */
end_comment

begin_comment
comment|// CHECKSTYLE:OFF
end_comment

begin_class
specifier|final
class|class
name|EntityArrays
block|{
comment|/**      * A Map&lt;CharSequence, CharSequence&gt; to to escape      *<a href="https://secure.wikimedia.org/wikipedia/en/wiki/ISO/IEC_8859-1">ISO-8859-1</a>      * characters to their named HTML 3.x equivalents.      */
specifier|public
specifier|static
specifier|final
name|Map
argument_list|<
name|CharSequence
argument_list|,
name|CharSequence
argument_list|>
name|ISO8859_1_ESCAPE
decl_stmt|;
static|static
block|{
specifier|final
name|Map
argument_list|<
name|CharSequence
argument_list|,
name|CharSequence
argument_list|>
name|initialMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00A0"
argument_list|,
literal|"&nbsp;"
argument_list|)
expr_stmt|;
comment|// non-breaking space
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00A1"
argument_list|,
literal|"&iexcl;"
argument_list|)
expr_stmt|;
comment|// inverted exclamation mark
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00A2"
argument_list|,
literal|"&cent;"
argument_list|)
expr_stmt|;
comment|// cent sign
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00A3"
argument_list|,
literal|"&pound;"
argument_list|)
expr_stmt|;
comment|// pound sign
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00A4"
argument_list|,
literal|"&curren;"
argument_list|)
expr_stmt|;
comment|// currency sign
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00A5"
argument_list|,
literal|"&yen;"
argument_list|)
expr_stmt|;
comment|// yen sign = yuan sign
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00A6"
argument_list|,
literal|"&brvbar;"
argument_list|)
expr_stmt|;
comment|// broken bar = broken vertical bar
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00A7"
argument_list|,
literal|"&sect;"
argument_list|)
expr_stmt|;
comment|// section sign
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00A8"
argument_list|,
literal|"&uml;"
argument_list|)
expr_stmt|;
comment|// diaeresis = spacing diaeresis
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00A9"
argument_list|,
literal|"&copy;"
argument_list|)
expr_stmt|;
comment|// © - copyright sign
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00AA"
argument_list|,
literal|"&ordf;"
argument_list|)
expr_stmt|;
comment|// feminine ordinal indicator
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00AB"
argument_list|,
literal|"&laquo;"
argument_list|)
expr_stmt|;
comment|// left-pointing double angle quotation mark = left pointing guillemet
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00AC"
argument_list|,
literal|"&not;"
argument_list|)
expr_stmt|;
comment|// not sign
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00AD"
argument_list|,
literal|"&shy;"
argument_list|)
expr_stmt|;
comment|// soft hyphen = discretionary hyphen
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00AE"
argument_list|,
literal|"&reg;"
argument_list|)
expr_stmt|;
comment|// ® - registered trademark sign
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00AF"
argument_list|,
literal|"&macr;"
argument_list|)
expr_stmt|;
comment|// macron = spacing macron = overline = APL overbar
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00B0"
argument_list|,
literal|"&deg;"
argument_list|)
expr_stmt|;
comment|// degree sign
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00B1"
argument_list|,
literal|"&plusmn;"
argument_list|)
expr_stmt|;
comment|// plus-minus sign = plus-or-minus sign
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00B2"
argument_list|,
literal|"&sup2;"
argument_list|)
expr_stmt|;
comment|// superscript two = superscript digit two = squared
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00B3"
argument_list|,
literal|"&sup3;"
argument_list|)
expr_stmt|;
comment|// superscript three = superscript digit three = cubed
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00B4"
argument_list|,
literal|"&acute;"
argument_list|)
expr_stmt|;
comment|// acute accent = spacing acute
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00B5"
argument_list|,
literal|"&micro;"
argument_list|)
expr_stmt|;
comment|// micro sign
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00B6"
argument_list|,
literal|"&para;"
argument_list|)
expr_stmt|;
comment|// pilcrow sign = paragraph sign
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00B7"
argument_list|,
literal|"&middot;"
argument_list|)
expr_stmt|;
comment|// middle dot = Georgian comma = Greek middle dot
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00B8"
argument_list|,
literal|"&cedil;"
argument_list|)
expr_stmt|;
comment|// cedilla = spacing cedilla
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00B9"
argument_list|,
literal|"&sup1;"
argument_list|)
expr_stmt|;
comment|// superscript one = superscript digit one
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00BA"
argument_list|,
literal|"&ordm;"
argument_list|)
expr_stmt|;
comment|// masculine ordinal indicator
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00BB"
argument_list|,
literal|"&raquo;"
argument_list|)
expr_stmt|;
comment|// right-pointing double angle quotation mark = right pointing guillemet
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00BC"
argument_list|,
literal|"&frac14;"
argument_list|)
expr_stmt|;
comment|// vulgar fraction one quarter = fraction one quarter
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00BD"
argument_list|,
literal|"&frac12;"
argument_list|)
expr_stmt|;
comment|// vulgar fraction one half = fraction one half
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00BE"
argument_list|,
literal|"&frac34;"
argument_list|)
expr_stmt|;
comment|// vulgar fraction three quarters = fraction three quarters
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00BF"
argument_list|,
literal|"&iquest;"
argument_list|)
expr_stmt|;
comment|// inverted question mark = turned question mark
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00C0"
argument_list|,
literal|"&Agrave;"
argument_list|)
expr_stmt|;
comment|// À - uppercase A, grave accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00C1"
argument_list|,
literal|"&Aacute;"
argument_list|)
expr_stmt|;
comment|// Á - uppercase A, acute accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00C2"
argument_list|,
literal|"&Acirc;"
argument_list|)
expr_stmt|;
comment|// Â - uppercase A, circumflex accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00C3"
argument_list|,
literal|"&Atilde;"
argument_list|)
expr_stmt|;
comment|// Ã - uppercase A, tilde
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00C4"
argument_list|,
literal|"&Auml;"
argument_list|)
expr_stmt|;
comment|// Ä - uppercase A, umlaut
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00C5"
argument_list|,
literal|"&Aring;"
argument_list|)
expr_stmt|;
comment|// Å - uppercase A, ring
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00C6"
argument_list|,
literal|"&AElig;"
argument_list|)
expr_stmt|;
comment|// Æ - uppercase AE
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00C7"
argument_list|,
literal|"&Ccedil;"
argument_list|)
expr_stmt|;
comment|// Ç - uppercase C, cedilla
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00C8"
argument_list|,
literal|"&Egrave;"
argument_list|)
expr_stmt|;
comment|// È - uppercase E, grave accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00C9"
argument_list|,
literal|"&Eacute;"
argument_list|)
expr_stmt|;
comment|// É - uppercase E, acute accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00CA"
argument_list|,
literal|"&Ecirc;"
argument_list|)
expr_stmt|;
comment|// Ê - uppercase E, circumflex accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00CB"
argument_list|,
literal|"&Euml;"
argument_list|)
expr_stmt|;
comment|// Ë - uppercase E, umlaut
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00CC"
argument_list|,
literal|"&Igrave;"
argument_list|)
expr_stmt|;
comment|// Ì - uppercase I, grave accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00CD"
argument_list|,
literal|"&Iacute;"
argument_list|)
expr_stmt|;
comment|// Í - uppercase I, acute accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00CE"
argument_list|,
literal|"&Icirc;"
argument_list|)
expr_stmt|;
comment|// Î - uppercase I, circumflex accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00CF"
argument_list|,
literal|"&Iuml;"
argument_list|)
expr_stmt|;
comment|// Ï - uppercase I, umlaut
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00D0"
argument_list|,
literal|"&ETH;"
argument_list|)
expr_stmt|;
comment|// Ð - uppercase Eth, Icelandic
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00D1"
argument_list|,
literal|"&Ntilde;"
argument_list|)
expr_stmt|;
comment|// Ñ - uppercase N, tilde
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00D2"
argument_list|,
literal|"&Ograve;"
argument_list|)
expr_stmt|;
comment|// Ò - uppercase O, grave accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00D3"
argument_list|,
literal|"&Oacute;"
argument_list|)
expr_stmt|;
comment|// Ó - uppercase O, acute accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00D4"
argument_list|,
literal|"&Ocirc;"
argument_list|)
expr_stmt|;
comment|// Ô - uppercase O, circumflex accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00D5"
argument_list|,
literal|"&Otilde;"
argument_list|)
expr_stmt|;
comment|// Õ - uppercase O, tilde
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00D6"
argument_list|,
literal|"&Ouml;"
argument_list|)
expr_stmt|;
comment|// Ö - uppercase O, umlaut
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00D7"
argument_list|,
literal|"&times;"
argument_list|)
expr_stmt|;
comment|// multiplication sign
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00D8"
argument_list|,
literal|"&Oslash;"
argument_list|)
expr_stmt|;
comment|// Ø - uppercase O, slash
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00D9"
argument_list|,
literal|"&Ugrave;"
argument_list|)
expr_stmt|;
comment|// Ù - uppercase U, grave accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00DA"
argument_list|,
literal|"&Uacute;"
argument_list|)
expr_stmt|;
comment|// Ú - uppercase U, acute accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00DB"
argument_list|,
literal|"&Ucirc;"
argument_list|)
expr_stmt|;
comment|// Û - uppercase U, circumflex accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00DC"
argument_list|,
literal|"&Uuml;"
argument_list|)
expr_stmt|;
comment|// Ü - uppercase U, umlaut
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00DD"
argument_list|,
literal|"&Yacute;"
argument_list|)
expr_stmt|;
comment|// Ý - uppercase Y, acute accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00DE"
argument_list|,
literal|"&THORN;"
argument_list|)
expr_stmt|;
comment|// Þ - uppercase THORN, Icelandic
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00DF"
argument_list|,
literal|"&szlig;"
argument_list|)
expr_stmt|;
comment|// ß - lowercase sharps, German
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00E0"
argument_list|,
literal|"&agrave;"
argument_list|)
expr_stmt|;
comment|// à - lowercase a, grave accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00E1"
argument_list|,
literal|"&aacute;"
argument_list|)
expr_stmt|;
comment|// á - lowercase a, acute accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00E2"
argument_list|,
literal|"&acirc;"
argument_list|)
expr_stmt|;
comment|// â - lowercase a, circumflex accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00E3"
argument_list|,
literal|"&atilde;"
argument_list|)
expr_stmt|;
comment|// ã - lowercase a, tilde
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00E4"
argument_list|,
literal|"&auml;"
argument_list|)
expr_stmt|;
comment|// ä - lowercase a, umlaut
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00E5"
argument_list|,
literal|"&aring;"
argument_list|)
expr_stmt|;
comment|// å - lowercase a, ring
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00E6"
argument_list|,
literal|"&aelig;"
argument_list|)
expr_stmt|;
comment|// æ - lowercase ae
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00E7"
argument_list|,
literal|"&ccedil;"
argument_list|)
expr_stmt|;
comment|// ç - lowercase c, cedilla
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00E8"
argument_list|,
literal|"&egrave;"
argument_list|)
expr_stmt|;
comment|// è - lowercase e, grave accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00E9"
argument_list|,
literal|"&eacute;"
argument_list|)
expr_stmt|;
comment|// é - lowercase e, acute accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00EA"
argument_list|,
literal|"&ecirc;"
argument_list|)
expr_stmt|;
comment|// ê - lowercase e, circumflex accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00EB"
argument_list|,
literal|"&euml;"
argument_list|)
expr_stmt|;
comment|// ë - lowercase e, umlaut
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00EC"
argument_list|,
literal|"&igrave;"
argument_list|)
expr_stmt|;
comment|// ì - lowercase i, grave accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00ED"
argument_list|,
literal|"&iacute;"
argument_list|)
expr_stmt|;
comment|// í - lowercase i, acute accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00EE"
argument_list|,
literal|"&icirc;"
argument_list|)
expr_stmt|;
comment|// î - lowercase i, circumflex accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00EF"
argument_list|,
literal|"&iuml;"
argument_list|)
expr_stmt|;
comment|// ï - lowercase i, umlaut
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00F0"
argument_list|,
literal|"&eth;"
argument_list|)
expr_stmt|;
comment|// ð - lowercase eth, Icelandic
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00F1"
argument_list|,
literal|"&ntilde;"
argument_list|)
expr_stmt|;
comment|// ñ - lowercase n, tilde
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00F2"
argument_list|,
literal|"&ograve;"
argument_list|)
expr_stmt|;
comment|// ò - lowercase o, grave accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00F3"
argument_list|,
literal|"&oacute;"
argument_list|)
expr_stmt|;
comment|// ó - lowercase o, acute accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00F4"
argument_list|,
literal|"&ocirc;"
argument_list|)
expr_stmt|;
comment|// ô - lowercase o, circumflex accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00F5"
argument_list|,
literal|"&otilde;"
argument_list|)
expr_stmt|;
comment|// õ - lowercase o, tilde
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00F6"
argument_list|,
literal|"&ouml;"
argument_list|)
expr_stmt|;
comment|// ö - lowercase o, umlaut
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00F7"
argument_list|,
literal|"&divide;"
argument_list|)
expr_stmt|;
comment|// division sign
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00F8"
argument_list|,
literal|"&oslash;"
argument_list|)
expr_stmt|;
comment|// ø - lowercase o, slash
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00F9"
argument_list|,
literal|"&ugrave;"
argument_list|)
expr_stmt|;
comment|// ù - lowercase u, grave accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00FA"
argument_list|,
literal|"&uacute;"
argument_list|)
expr_stmt|;
comment|// ú - lowercase u, acute accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00FB"
argument_list|,
literal|"&ucirc;"
argument_list|)
expr_stmt|;
comment|// û - lowercase u, circumflex accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00FC"
argument_list|,
literal|"&uuml;"
argument_list|)
expr_stmt|;
comment|// ü - lowercase u, umlaut
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00FD"
argument_list|,
literal|"&yacute;"
argument_list|)
expr_stmt|;
comment|// ý - lowercase y, acute accent
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00FE"
argument_list|,
literal|"&thorn;"
argument_list|)
expr_stmt|;
comment|// þ - lowercase thorn, Icelandic
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u00FF"
argument_list|,
literal|"&yuml;"
argument_list|)
expr_stmt|;
comment|// ÿ - lowercase y, umlaut
name|ISO8859_1_ESCAPE
operator|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|initialMap
argument_list|)
expr_stmt|;
block|}
comment|/**      * Reverse of {@link #ISO8859_1_ESCAPE} for unescaping purposes.      */
specifier|public
specifier|static
specifier|final
name|Map
argument_list|<
name|CharSequence
argument_list|,
name|CharSequence
argument_list|>
name|ISO8859_1_UNESCAPE
decl_stmt|;
static|static
block|{
name|ISO8859_1_UNESCAPE
operator|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|invert
argument_list|(
name|ISO8859_1_ESCAPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * A Map&lt;CharSequence, CharSequence&gt; to escape additional      *<a href="http://www.w3.org/TR/REC-html40/sgml/entities.html">character entity      * references</a>. Note that this must be used with {@link #ISO8859_1_ESCAPE} to get the full list of      * HTML 4.0 character entities.      */
specifier|public
specifier|static
specifier|final
name|Map
argument_list|<
name|CharSequence
argument_list|,
name|CharSequence
argument_list|>
name|HTML40_EXTENDED_ESCAPE
decl_stmt|;
static|static
block|{
specifier|final
name|Map
argument_list|<
name|CharSequence
argument_list|,
name|CharSequence
argument_list|>
name|initialMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|//<!-- Latin Extended-B -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u0192"
argument_list|,
literal|"&fnof;"
argument_list|)
expr_stmt|;
comment|// latin small f with hook = function= florin, U+0192 ISOtech -->
comment|//<!-- Greek -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u0391"
argument_list|,
literal|"&Alpha;"
argument_list|)
expr_stmt|;
comment|// greek capital letter alpha, U+0391 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u0392"
argument_list|,
literal|"&Beta;"
argument_list|)
expr_stmt|;
comment|// greek capital letter beta, U+0392 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u0393"
argument_list|,
literal|"&Gamma;"
argument_list|)
expr_stmt|;
comment|// greek capital letter gamma,U+0393 ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u0394"
argument_list|,
literal|"&Delta;"
argument_list|)
expr_stmt|;
comment|// greek capital letter delta,U+0394 ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u0395"
argument_list|,
literal|"&Epsilon;"
argument_list|)
expr_stmt|;
comment|// greek capital letter epsilon, U+0395 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u0396"
argument_list|,
literal|"&Zeta;"
argument_list|)
expr_stmt|;
comment|// greek capital letter zeta, U+0396 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u0397"
argument_list|,
literal|"&Eta;"
argument_list|)
expr_stmt|;
comment|// greek capital letter eta, U+0397 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u0398"
argument_list|,
literal|"&Theta;"
argument_list|)
expr_stmt|;
comment|// greek capital letter theta,U+0398 ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u0399"
argument_list|,
literal|"&Iota;"
argument_list|)
expr_stmt|;
comment|// greek capital letter iota, U+0399 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u039A"
argument_list|,
literal|"&Kappa;"
argument_list|)
expr_stmt|;
comment|// greek capital letter kappa, U+039A -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u039B"
argument_list|,
literal|"&Lambda;"
argument_list|)
expr_stmt|;
comment|// greek capital letter lambda,U+039B ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u039C"
argument_list|,
literal|"&Mu;"
argument_list|)
expr_stmt|;
comment|// greek capital letter mu, U+039C -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u039D"
argument_list|,
literal|"&Nu;"
argument_list|)
expr_stmt|;
comment|// greek capital letter nu, U+039D -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u039E"
argument_list|,
literal|"&Xi;"
argument_list|)
expr_stmt|;
comment|// greek capital letter xi, U+039E ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u039F"
argument_list|,
literal|"&Omicron;"
argument_list|)
expr_stmt|;
comment|// greek capital letter omicron, U+039F -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03A0"
argument_list|,
literal|"&Pi;"
argument_list|)
expr_stmt|;
comment|// greek capital letter pi, U+03A0 ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03A1"
argument_list|,
literal|"&Rho;"
argument_list|)
expr_stmt|;
comment|// greek capital letter rho, U+03A1 -->
comment|//<!-- there is no Sigmaf, and no U+03A2 character either -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03A3"
argument_list|,
literal|"&Sigma;"
argument_list|)
expr_stmt|;
comment|// greek capital letter sigma,U+03A3 ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03A4"
argument_list|,
literal|"&Tau;"
argument_list|)
expr_stmt|;
comment|// greek capital letter tau, U+03A4 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03A5"
argument_list|,
literal|"&Upsilon;"
argument_list|)
expr_stmt|;
comment|// greek capital letter upsilon,U+03A5 ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03A6"
argument_list|,
literal|"&Phi;"
argument_list|)
expr_stmt|;
comment|// greek capital letter phi,U+03A6 ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03A7"
argument_list|,
literal|"&Chi;"
argument_list|)
expr_stmt|;
comment|// greek capital letter chi, U+03A7 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03A8"
argument_list|,
literal|"&Psi;"
argument_list|)
expr_stmt|;
comment|// greek capital letter psi,U+03A8 ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03A9"
argument_list|,
literal|"&Omega;"
argument_list|)
expr_stmt|;
comment|// greek capital letter omega,U+03A9 ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03B1"
argument_list|,
literal|"&alpha;"
argument_list|)
expr_stmt|;
comment|// greek small letter alpha,U+03B1 ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03B2"
argument_list|,
literal|"&beta;"
argument_list|)
expr_stmt|;
comment|// greek small letter beta, U+03B2 ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03B3"
argument_list|,
literal|"&gamma;"
argument_list|)
expr_stmt|;
comment|// greek small letter gamma,U+03B3 ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03B4"
argument_list|,
literal|"&delta;"
argument_list|)
expr_stmt|;
comment|// greek small letter delta,U+03B4 ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03B5"
argument_list|,
literal|"&epsilon;"
argument_list|)
expr_stmt|;
comment|// greek small letter epsilon,U+03B5 ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03B6"
argument_list|,
literal|"&zeta;"
argument_list|)
expr_stmt|;
comment|// greek small letter zeta, U+03B6 ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03B7"
argument_list|,
literal|"&eta;"
argument_list|)
expr_stmt|;
comment|// greek small letter eta, U+03B7 ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03B8"
argument_list|,
literal|"&theta;"
argument_list|)
expr_stmt|;
comment|// greek small letter theta,U+03B8 ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03B9"
argument_list|,
literal|"&iota;"
argument_list|)
expr_stmt|;
comment|// greek small letter iota, U+03B9 ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03BA"
argument_list|,
literal|"&kappa;"
argument_list|)
expr_stmt|;
comment|// greek small letter kappa,U+03BA ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03BB"
argument_list|,
literal|"&lambda;"
argument_list|)
expr_stmt|;
comment|// greek small letter lambda,U+03BB ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03BC"
argument_list|,
literal|"&mu;"
argument_list|)
expr_stmt|;
comment|// greek small letter mu, U+03BC ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03BD"
argument_list|,
literal|"&nu;"
argument_list|)
expr_stmt|;
comment|// greek small letter nu, U+03BD ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03BE"
argument_list|,
literal|"&xi;"
argument_list|)
expr_stmt|;
comment|// greek small letter xi, U+03BE ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03BF"
argument_list|,
literal|"&omicron;"
argument_list|)
expr_stmt|;
comment|// greek small letter omicron, U+03BF NEW -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03C0"
argument_list|,
literal|"&pi;"
argument_list|)
expr_stmt|;
comment|// greek small letter pi, U+03C0 ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03C1"
argument_list|,
literal|"&rho;"
argument_list|)
expr_stmt|;
comment|// greek small letter rho, U+03C1 ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03C2"
argument_list|,
literal|"&sigmaf;"
argument_list|)
expr_stmt|;
comment|// greek small letter final sigma,U+03C2 ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03C3"
argument_list|,
literal|"&sigma;"
argument_list|)
expr_stmt|;
comment|// greek small letter sigma,U+03C3 ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03C4"
argument_list|,
literal|"&tau;"
argument_list|)
expr_stmt|;
comment|// greek small letter tau, U+03C4 ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03C5"
argument_list|,
literal|"&upsilon;"
argument_list|)
expr_stmt|;
comment|// greek small letter upsilon,U+03C5 ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03C6"
argument_list|,
literal|"&phi;"
argument_list|)
expr_stmt|;
comment|// greek small letter phi, U+03C6 ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03C7"
argument_list|,
literal|"&chi;"
argument_list|)
expr_stmt|;
comment|// greek small letter chi, U+03C7 ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03C8"
argument_list|,
literal|"&psi;"
argument_list|)
expr_stmt|;
comment|// greek small letter psi, U+03C8 ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03C9"
argument_list|,
literal|"&omega;"
argument_list|)
expr_stmt|;
comment|// greek small letter omega,U+03C9 ISOgrk3 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03D1"
argument_list|,
literal|"&thetasym;"
argument_list|)
expr_stmt|;
comment|// greek small letter theta symbol,U+03D1 NEW -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03D2"
argument_list|,
literal|"&upsih;"
argument_list|)
expr_stmt|;
comment|// greek upsilon with hook symbol,U+03D2 NEW -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u03D6"
argument_list|,
literal|"&piv;"
argument_list|)
expr_stmt|;
comment|// greek pi symbol, U+03D6 ISOgrk3 -->
comment|//<!-- General Punctuation -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2022"
argument_list|,
literal|"&bull;"
argument_list|)
expr_stmt|;
comment|// bullet = black small circle,U+2022 ISOpub -->
comment|//<!-- bullet is NOT the same as bullet operator, U+2219 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2026"
argument_list|,
literal|"&hellip;"
argument_list|)
expr_stmt|;
comment|// horizontal ellipsis = three dot leader,U+2026 ISOpub -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2032"
argument_list|,
literal|"&prime;"
argument_list|)
expr_stmt|;
comment|// prime = minutes = feet, U+2032 ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2033"
argument_list|,
literal|"&Prime;"
argument_list|)
expr_stmt|;
comment|// double prime = seconds = inches,U+2033 ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u203E"
argument_list|,
literal|"&oline;"
argument_list|)
expr_stmt|;
comment|// overline = spacing overscore,U+203E NEW -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2044"
argument_list|,
literal|"&frasl;"
argument_list|)
expr_stmt|;
comment|// fraction slash, U+2044 NEW -->
comment|//<!-- Letterlike Symbols -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2118"
argument_list|,
literal|"&weierp;"
argument_list|)
expr_stmt|;
comment|// script capital P = power set= Weierstrass p, U+2118 ISOamso -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2111"
argument_list|,
literal|"&image;"
argument_list|)
expr_stmt|;
comment|// blackletter capital I = imaginary part,U+2111 ISOamso -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u211C"
argument_list|,
literal|"&real;"
argument_list|)
expr_stmt|;
comment|// blackletter capital R = real part symbol,U+211C ISOamso -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2122"
argument_list|,
literal|"&trade;"
argument_list|)
expr_stmt|;
comment|// trade mark sign, U+2122 ISOnum -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2135"
argument_list|,
literal|"&alefsym;"
argument_list|)
expr_stmt|;
comment|// alef symbol = first transfinite cardinal,U+2135 NEW -->
comment|//<!-- alef symbol is NOT the same as hebrew letter alef,U+05D0 although the
comment|// same glyph could be used to depict both characters -->
comment|//<!-- Arrows -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2190"
argument_list|,
literal|"&larr;"
argument_list|)
expr_stmt|;
comment|// leftwards arrow, U+2190 ISOnum -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2191"
argument_list|,
literal|"&uarr;"
argument_list|)
expr_stmt|;
comment|// upwards arrow, U+2191 ISOnum-->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2192"
argument_list|,
literal|"&rarr;"
argument_list|)
expr_stmt|;
comment|// rightwards arrow, U+2192 ISOnum -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2193"
argument_list|,
literal|"&darr;"
argument_list|)
expr_stmt|;
comment|// downwards arrow, U+2193 ISOnum -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2194"
argument_list|,
literal|"&harr;"
argument_list|)
expr_stmt|;
comment|// left right arrow, U+2194 ISOamsa -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u21B5"
argument_list|,
literal|"&crarr;"
argument_list|)
expr_stmt|;
comment|// downwards arrow with corner leftwards= carriage return, U+21B5 NEW -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u21D0"
argument_list|,
literal|"&lArr;"
argument_list|)
expr_stmt|;
comment|// leftwards double arrow, U+21D0 ISOtech -->
comment|//<!-- ISO 10646 does not say that lArr is the same as the 'is implied by'
comment|// arrow but also does not have any other character for that function.
comment|// So ? lArr canbe used for 'is implied by' as ISOtech suggests -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u21D1"
argument_list|,
literal|"&uArr;"
argument_list|)
expr_stmt|;
comment|// upwards double arrow, U+21D1 ISOamsa -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u21D2"
argument_list|,
literal|"&rArr;"
argument_list|)
expr_stmt|;
comment|// rightwards double arrow,U+21D2 ISOtech -->
comment|//<!-- ISO 10646 does not say this is the 'implies' character but does not
comment|// have another character with this function so ?rArr can be used for
comment|// 'implies' as ISOtech suggests -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u21D3"
argument_list|,
literal|"&dArr;"
argument_list|)
expr_stmt|;
comment|// downwards double arrow, U+21D3 ISOamsa -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u21D4"
argument_list|,
literal|"&hArr;"
argument_list|)
expr_stmt|;
comment|// left right double arrow,U+21D4 ISOamsa -->
comment|//<!-- Mathematical Operators -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2200"
argument_list|,
literal|"&forall;"
argument_list|)
expr_stmt|;
comment|// for all, U+2200 ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2202"
argument_list|,
literal|"&part;"
argument_list|)
expr_stmt|;
comment|// partial differential, U+2202 ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2203"
argument_list|,
literal|"&exist;"
argument_list|)
expr_stmt|;
comment|// there exists, U+2203 ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2205"
argument_list|,
literal|"&empty;"
argument_list|)
expr_stmt|;
comment|// empty set = null set = diameter,U+2205 ISOamso -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2207"
argument_list|,
literal|"&nabla;"
argument_list|)
expr_stmt|;
comment|// nabla = backward difference,U+2207 ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2208"
argument_list|,
literal|"&isin;"
argument_list|)
expr_stmt|;
comment|// element of, U+2208 ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2209"
argument_list|,
literal|"&notin;"
argument_list|)
expr_stmt|;
comment|// not an element of, U+2209 ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u220B"
argument_list|,
literal|"&ni;"
argument_list|)
expr_stmt|;
comment|// contains as member, U+220B ISOtech -->
comment|//<!-- should there be a more memorable name than 'ni'? -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u220F"
argument_list|,
literal|"&prod;"
argument_list|)
expr_stmt|;
comment|// n-ary product = product sign,U+220F ISOamsb -->
comment|//<!-- prod is NOT the same character as U+03A0 'greek capital letter pi'
comment|// though the same glyph might be used for both -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2211"
argument_list|,
literal|"&sum;"
argument_list|)
expr_stmt|;
comment|// n-ary summation, U+2211 ISOamsb -->
comment|//<!-- sum is NOT the same character as U+03A3 'greek capital letter sigma'
comment|// though the same glyph might be used for both -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2212"
argument_list|,
literal|"&minus;"
argument_list|)
expr_stmt|;
comment|// minus sign, U+2212 ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2217"
argument_list|,
literal|"&lowast;"
argument_list|)
expr_stmt|;
comment|// asterisk operator, U+2217 ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u221A"
argument_list|,
literal|"&radic;"
argument_list|)
expr_stmt|;
comment|// square root = radical sign,U+221A ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u221D"
argument_list|,
literal|"&prop;"
argument_list|)
expr_stmt|;
comment|// proportional to, U+221D ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u221E"
argument_list|,
literal|"&infin;"
argument_list|)
expr_stmt|;
comment|// infinity, U+221E ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2220"
argument_list|,
literal|"&ang;"
argument_list|)
expr_stmt|;
comment|// angle, U+2220 ISOamso -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2227"
argument_list|,
literal|"&and;"
argument_list|)
expr_stmt|;
comment|// logical and = wedge, U+2227 ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2228"
argument_list|,
literal|"&or;"
argument_list|)
expr_stmt|;
comment|// logical or = vee, U+2228 ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2229"
argument_list|,
literal|"&cap;"
argument_list|)
expr_stmt|;
comment|// intersection = cap, U+2229 ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u222A"
argument_list|,
literal|"&cup;"
argument_list|)
expr_stmt|;
comment|// union = cup, U+222A ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u222B"
argument_list|,
literal|"&int;"
argument_list|)
expr_stmt|;
comment|// integral, U+222B ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2234"
argument_list|,
literal|"&there4;"
argument_list|)
expr_stmt|;
comment|// therefore, U+2234 ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u223C"
argument_list|,
literal|"&sim;"
argument_list|)
expr_stmt|;
comment|// tilde operator = varies with = similar to,U+223C ISOtech -->
comment|//<!-- tilde operator is NOT the same character as the tilde, U+007E,although
comment|// the same glyph might be used to represent both -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2245"
argument_list|,
literal|"&cong;"
argument_list|)
expr_stmt|;
comment|// approximately equal to, U+2245 ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2248"
argument_list|,
literal|"&asymp;"
argument_list|)
expr_stmt|;
comment|// almost equal to = asymptotic to,U+2248 ISOamsr -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2260"
argument_list|,
literal|"&ne;"
argument_list|)
expr_stmt|;
comment|// not equal to, U+2260 ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2261"
argument_list|,
literal|"&equiv;"
argument_list|)
expr_stmt|;
comment|// identical to, U+2261 ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2264"
argument_list|,
literal|"&le;"
argument_list|)
expr_stmt|;
comment|// less-than or equal to, U+2264 ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2265"
argument_list|,
literal|"&ge;"
argument_list|)
expr_stmt|;
comment|// greater-than or equal to,U+2265 ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2282"
argument_list|,
literal|"&sub;"
argument_list|)
expr_stmt|;
comment|// subset of, U+2282 ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2283"
argument_list|,
literal|"&sup;"
argument_list|)
expr_stmt|;
comment|// superset of, U+2283 ISOtech -->
comment|//<!-- note that nsup, 'not a superset of, U+2283' is not covered by the
comment|// Symbol font encoding and is not included. Should it be, for symmetry?
comment|// It is in ISOamsn -->,
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2284"
argument_list|,
literal|"&nsub;"
argument_list|)
expr_stmt|;
comment|// not a subset of, U+2284 ISOamsn -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2286"
argument_list|,
literal|"&sube;"
argument_list|)
expr_stmt|;
comment|// subset of or equal to, U+2286 ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2287"
argument_list|,
literal|"&supe;"
argument_list|)
expr_stmt|;
comment|// superset of or equal to,U+2287 ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2295"
argument_list|,
literal|"&oplus;"
argument_list|)
expr_stmt|;
comment|// circled plus = direct sum,U+2295 ISOamsb -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2297"
argument_list|,
literal|"&otimes;"
argument_list|)
expr_stmt|;
comment|// circled times = vector product,U+2297 ISOamsb -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u22A5"
argument_list|,
literal|"&perp;"
argument_list|)
expr_stmt|;
comment|// up tack = orthogonal to = perpendicular,U+22A5 ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u22C5"
argument_list|,
literal|"&sdot;"
argument_list|)
expr_stmt|;
comment|// dot operator, U+22C5 ISOamsb -->
comment|//<!-- dot operator is NOT the same character as U+00B7 middle dot -->
comment|//<!-- Miscellaneous Technical -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2308"
argument_list|,
literal|"&lceil;"
argument_list|)
expr_stmt|;
comment|// left ceiling = apl upstile,U+2308 ISOamsc -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2309"
argument_list|,
literal|"&rceil;"
argument_list|)
expr_stmt|;
comment|// right ceiling, U+2309 ISOamsc -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u230A"
argument_list|,
literal|"&lfloor;"
argument_list|)
expr_stmt|;
comment|// left floor = apl downstile,U+230A ISOamsc -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u230B"
argument_list|,
literal|"&rfloor;"
argument_list|)
expr_stmt|;
comment|// right floor, U+230B ISOamsc -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2329"
argument_list|,
literal|"&lang;"
argument_list|)
expr_stmt|;
comment|// left-pointing angle bracket = bra,U+2329 ISOtech -->
comment|//<!-- lang is NOT the same character as U+003C 'less than' or U+2039 'single left-pointing angle quotation
comment|// mark' -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u232A"
argument_list|,
literal|"&rang;"
argument_list|)
expr_stmt|;
comment|// right-pointing angle bracket = ket,U+232A ISOtech -->
comment|//<!-- rang is NOT the same character as U+003E 'greater than' or U+203A
comment|// 'single right-pointing angle quotation mark' -->
comment|//<!-- Geometric Shapes -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u25CA"
argument_list|,
literal|"&loz;"
argument_list|)
expr_stmt|;
comment|// lozenge, U+25CA ISOpub -->
comment|//<!-- Miscellaneous Symbols -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2660"
argument_list|,
literal|"&spades;"
argument_list|)
expr_stmt|;
comment|// black spade suit, U+2660 ISOpub -->
comment|//<!-- black here seems to mean filled as opposed to hollow -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2663"
argument_list|,
literal|"&clubs;"
argument_list|)
expr_stmt|;
comment|// black club suit = shamrock,U+2663 ISOpub -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2665"
argument_list|,
literal|"&hearts;"
argument_list|)
expr_stmt|;
comment|// black heart suit = valentine,U+2665 ISOpub -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2666"
argument_list|,
literal|"&diams;"
argument_list|)
expr_stmt|;
comment|// black diamond suit, U+2666 ISOpub -->
comment|//<!-- Latin Extended-A -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u0152"
argument_list|,
literal|"&OElig;"
argument_list|)
expr_stmt|;
comment|// -- latin capital ligature OE,U+0152 ISOlat2 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u0153"
argument_list|,
literal|"&oelig;"
argument_list|)
expr_stmt|;
comment|// -- latin small ligature oe, U+0153 ISOlat2 -->
comment|//<!-- ligature is a misnomer, this is a separate character in some languages -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u0160"
argument_list|,
literal|"&Scaron;"
argument_list|)
expr_stmt|;
comment|// -- latin capital letter S with caron,U+0160 ISOlat2 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u0161"
argument_list|,
literal|"&scaron;"
argument_list|)
expr_stmt|;
comment|// -- latin small letter s with caron,U+0161 ISOlat2 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u0178"
argument_list|,
literal|"&Yuml;"
argument_list|)
expr_stmt|;
comment|// -- latin capital letter Y with diaeresis,U+0178 ISOlat2 -->
comment|//<!-- Spacing Modifier Letters -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u02C6"
argument_list|,
literal|"&circ;"
argument_list|)
expr_stmt|;
comment|// -- modifier letter circumflex accent,U+02C6 ISOpub -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u02DC"
argument_list|,
literal|"&tilde;"
argument_list|)
expr_stmt|;
comment|// small tilde, U+02DC ISOdia -->
comment|//<!-- General Punctuation -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2002"
argument_list|,
literal|"&ensp;"
argument_list|)
expr_stmt|;
comment|// en space, U+2002 ISOpub -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2003"
argument_list|,
literal|"&emsp;"
argument_list|)
expr_stmt|;
comment|// em space, U+2003 ISOpub -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2009"
argument_list|,
literal|"&thinsp;"
argument_list|)
expr_stmt|;
comment|// thin space, U+2009 ISOpub -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u200C"
argument_list|,
literal|"&zwnj;"
argument_list|)
expr_stmt|;
comment|// zero width non-joiner,U+200C NEW RFC 2070 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u200D"
argument_list|,
literal|"&zwj;"
argument_list|)
expr_stmt|;
comment|// zero width joiner, U+200D NEW RFC 2070 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u200E"
argument_list|,
literal|"&lrm;"
argument_list|)
expr_stmt|;
comment|// left-to-right mark, U+200E NEW RFC 2070 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u200F"
argument_list|,
literal|"&rlm;"
argument_list|)
expr_stmt|;
comment|// right-to-left mark, U+200F NEW RFC 2070 -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2013"
argument_list|,
literal|"&ndash;"
argument_list|)
expr_stmt|;
comment|// en dash, U+2013 ISOpub -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2014"
argument_list|,
literal|"&mdash;"
argument_list|)
expr_stmt|;
comment|// em dash, U+2014 ISOpub -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2018"
argument_list|,
literal|"&lsquo;"
argument_list|)
expr_stmt|;
comment|// left single quotation mark,U+2018 ISOnum -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2019"
argument_list|,
literal|"&rsquo;"
argument_list|)
expr_stmt|;
comment|// right single quotation mark,U+2019 ISOnum -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u201A"
argument_list|,
literal|"&sbquo;"
argument_list|)
expr_stmt|;
comment|// single low-9 quotation mark, U+201A NEW -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u201C"
argument_list|,
literal|"&ldquo;"
argument_list|)
expr_stmt|;
comment|// left double quotation mark,U+201C ISOnum -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u201D"
argument_list|,
literal|"&rdquo;"
argument_list|)
expr_stmt|;
comment|// right double quotation mark,U+201D ISOnum -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u201E"
argument_list|,
literal|"&bdquo;"
argument_list|)
expr_stmt|;
comment|// double low-9 quotation mark, U+201E NEW -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2020"
argument_list|,
literal|"&dagger;"
argument_list|)
expr_stmt|;
comment|// dagger, U+2020 ISOpub -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2021"
argument_list|,
literal|"&Dagger;"
argument_list|)
expr_stmt|;
comment|// double dagger, U+2021 ISOpub -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2030"
argument_list|,
literal|"&permil;"
argument_list|)
expr_stmt|;
comment|// per mille sign, U+2030 ISOtech -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u2039"
argument_list|,
literal|"&lsaquo;"
argument_list|)
expr_stmt|;
comment|// single left-pointing angle quotation mark,U+2039 ISO proposed -->
comment|//<!-- lsaquo is proposed but not yet ISO standardized -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u203A"
argument_list|,
literal|"&rsaquo;"
argument_list|)
expr_stmt|;
comment|// single right-pointing angle quotation mark,U+203A ISO proposed -->
comment|//<!-- rsaquo is proposed but not yet ISO standardized -->
name|initialMap
operator|.
name|put
argument_list|(
literal|"\u20AC"
argument_list|,
literal|"&euro;"
argument_list|)
expr_stmt|;
comment|// -- euro sign, U+20AC NEW -->
name|HTML40_EXTENDED_ESCAPE
operator|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|initialMap
argument_list|)
expr_stmt|;
block|}
comment|/**      * Reverse of {@link #HTML40_EXTENDED_ESCAPE} for unescaping purposes.      */
specifier|public
specifier|static
specifier|final
name|Map
argument_list|<
name|CharSequence
argument_list|,
name|CharSequence
argument_list|>
name|HTML40_EXTENDED_UNESCAPE
decl_stmt|;
static|static
block|{
name|HTML40_EXTENDED_UNESCAPE
operator|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|invert
argument_list|(
name|HTML40_EXTENDED_ESCAPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * A Map&lt;CharSequence, CharSequence&gt; to escape the basic XML and HTML      * character entities.      *      * Namely: {@code "&<>}      */
specifier|public
specifier|static
specifier|final
name|Map
argument_list|<
name|CharSequence
argument_list|,
name|CharSequence
argument_list|>
name|BASIC_ESCAPE
decl_stmt|;
static|static
block|{
specifier|final
name|Map
argument_list|<
name|CharSequence
argument_list|,
name|CharSequence
argument_list|>
name|initialMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|initialMap
operator|.
name|put
argument_list|(
literal|"\""
argument_list|,
literal|"&quot;"
argument_list|)
expr_stmt|;
comment|// " - double-quote
name|initialMap
operator|.
name|put
argument_list|(
literal|"&"
argument_list|,
literal|"&amp;"
argument_list|)
expr_stmt|;
comment|//& - ampersand
name|initialMap
operator|.
name|put
argument_list|(
literal|"<"
argument_list|,
literal|"&lt;"
argument_list|)
expr_stmt|;
comment|//< - less-than
name|initialMap
operator|.
name|put
argument_list|(
literal|">"
argument_list|,
literal|"&gt;"
argument_list|)
expr_stmt|;
comment|//> - greater-than
name|BASIC_ESCAPE
operator|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|initialMap
argument_list|)
expr_stmt|;
block|}
comment|/**      * Reverse of {@link #BASIC_ESCAPE} for unescaping purposes.      */
specifier|public
specifier|static
specifier|final
name|Map
argument_list|<
name|CharSequence
argument_list|,
name|CharSequence
argument_list|>
name|BASIC_UNESCAPE
decl_stmt|;
static|static
block|{
name|BASIC_UNESCAPE
operator|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|invert
argument_list|(
name|BASIC_ESCAPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * A Map&lt;CharSequence, CharSequence&gt; to escape the apostrophe character to      * its XML character entity.      */
specifier|public
specifier|static
specifier|final
name|Map
argument_list|<
name|CharSequence
argument_list|,
name|CharSequence
argument_list|>
name|APOS_ESCAPE
decl_stmt|;
static|static
block|{
specifier|final
name|Map
argument_list|<
name|CharSequence
argument_list|,
name|CharSequence
argument_list|>
name|initialMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|initialMap
operator|.
name|put
argument_list|(
literal|"'"
argument_list|,
literal|"&apos;"
argument_list|)
expr_stmt|;
comment|// XML apostrophe
name|APOS_ESCAPE
operator|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|initialMap
argument_list|)
expr_stmt|;
block|}
comment|/**      * Reverse of {@link #APOS_ESCAPE} for unescaping purposes.      */
specifier|public
specifier|static
specifier|final
name|Map
argument_list|<
name|CharSequence
argument_list|,
name|CharSequence
argument_list|>
name|APOS_UNESCAPE
decl_stmt|;
static|static
block|{
name|APOS_UNESCAPE
operator|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|invert
argument_list|(
name|APOS_ESCAPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * A Map&lt;CharSequence, CharSequence&gt; to escape the Java      * control characters.      *      * Namely: {@code \b \n \t \f \r}      */
specifier|public
specifier|static
specifier|final
name|Map
argument_list|<
name|CharSequence
argument_list|,
name|CharSequence
argument_list|>
name|JAVA_CTRL_CHARS_ESCAPE
decl_stmt|;
static|static
block|{
specifier|final
name|Map
argument_list|<
name|CharSequence
argument_list|,
name|CharSequence
argument_list|>
name|initialMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|initialMap
operator|.
name|put
argument_list|(
literal|"\b"
argument_list|,
literal|"\\b"
argument_list|)
expr_stmt|;
name|initialMap
operator|.
name|put
argument_list|(
literal|"\n"
argument_list|,
literal|"\\n"
argument_list|)
expr_stmt|;
name|initialMap
operator|.
name|put
argument_list|(
literal|"\t"
argument_list|,
literal|"\\t"
argument_list|)
expr_stmt|;
name|initialMap
operator|.
name|put
argument_list|(
literal|"\f"
argument_list|,
literal|"\\f"
argument_list|)
expr_stmt|;
name|initialMap
operator|.
name|put
argument_list|(
literal|"\r"
argument_list|,
literal|"\\r"
argument_list|)
expr_stmt|;
name|JAVA_CTRL_CHARS_ESCAPE
operator|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|initialMap
argument_list|)
expr_stmt|;
block|}
comment|/**      * Reverse of {@link #JAVA_CTRL_CHARS_ESCAPE} for unescaping purposes.      */
specifier|public
specifier|static
specifier|final
name|Map
argument_list|<
name|CharSequence
argument_list|,
name|CharSequence
argument_list|>
name|JAVA_CTRL_CHARS_UNESCAPE
decl_stmt|;
static|static
block|{
name|JAVA_CTRL_CHARS_UNESCAPE
operator|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|invert
argument_list|(
name|JAVA_CTRL_CHARS_ESCAPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|EntityArrays
parameter_list|()
block|{
comment|// complete
block|}
comment|/**      * Used to invert an escape Map into an unescape Map.      * @param map Map&lt;String, String&gt; to be inverted      * @return Map&lt;String, String&gt; inverted array      */
specifier|public
specifier|static
name|Map
argument_list|<
name|CharSequence
argument_list|,
name|CharSequence
argument_list|>
name|invert
parameter_list|(
specifier|final
name|Map
argument_list|<
name|CharSequence
argument_list|,
name|CharSequence
argument_list|>
name|map
parameter_list|)
block|{
specifier|final
name|Map
argument_list|<
name|CharSequence
argument_list|,
name|CharSequence
argument_list|>
name|newMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|CharSequence
argument_list|,
name|CharSequence
argument_list|>
name|pair
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|newMap
operator|.
name|put
argument_list|(
name|pair
operator|.
name|getValue
argument_list|()
argument_list|,
name|pair
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|newMap
return|;
block|}
block|}
end_class

end_unit

