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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_comment
comment|/**  * An API for translating text.  * Its core use is to escape and unescape text. Because escaping and unescaping  * is completely contextual, the API does not present two separate signatures.  *  * @since 1.0  */
end_comment

begin_class
specifier|abstract
class|class
name|CharSequenceTranslator
block|{
comment|/**      * Array containing the hexadecimal alphabet.      */
specifier|static
specifier|final
name|char
index|[]
name|HEX_DIGITS
init|=
operator|new
name|char
index|[]
block|{
literal|'0'
block|,
literal|'1'
block|,
literal|'2'
block|,
literal|'3'
block|,
literal|'4'
block|,
literal|'5'
block|,
literal|'6'
block|,
literal|'7'
block|,
literal|'8'
block|,
literal|'9'
block|,
literal|'A'
block|,
literal|'B'
block|,
literal|'C'
block|,
literal|'D'
block|,
literal|'E'
block|,
literal|'F'
block|}
decl_stmt|;
comment|/**      * Translate a set of codepoints, represented by an int index into a CharSequence,      * into another set of codepoints. The number of codepoints consumed must be returned,      * and the only IOExceptions thrown must be from interacting with the Writer so that      * the top level API may reliably ignore StringWriter IOExceptions.      *      * @param input CharSequence that is being translated      * @param index int representing the current point of translation      * @param out Writer to translate the text to      * @return int count of codepoints consumed      * @throws IOException if and only if the Writer produces an IOException      */
specifier|public
specifier|abstract
name|int
name|translate
parameter_list|(
name|CharSequence
name|input
parameter_list|,
name|int
name|index
parameter_list|,
name|Writer
name|out
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Helper for non-Writer usage.      * @param input CharSequence to be translated      * @return String output of translation      */
specifier|public
specifier|final
name|String
name|translate
parameter_list|(
specifier|final
name|CharSequence
name|input
parameter_list|)
block|{
if|if
condition|(
name|input
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
specifier|final
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|(
name|input
operator|.
name|length
argument_list|()
operator|*
literal|2
argument_list|)
decl_stmt|;
name|translate
argument_list|(
name|input
argument_list|,
name|writer
argument_list|)
expr_stmt|;
return|return
name|writer
operator|.
name|toString
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
specifier|final
name|IOException
name|ioe
parameter_list|)
block|{
comment|// this should never ever happen while writing to a StringWriter
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ioe
argument_list|)
throw|;
block|}
block|}
comment|/**      * Translate an input onto a Writer. This is intentionally final as its algorithm is      * tightly coupled with the abstract method of this class.      *      * @param input CharSequence that is being translated      * @param out Writer to translate the text to      * @throws IOException if and only if the Writer produces an IOException      */
specifier|public
specifier|final
name|void
name|translate
parameter_list|(
specifier|final
name|CharSequence
name|input
parameter_list|,
specifier|final
name|Writer
name|out
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|input
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|int
name|pos
init|=
literal|0
decl_stmt|;
specifier|final
name|int
name|len
init|=
name|input
operator|.
name|length
argument_list|()
decl_stmt|;
while|while
condition|(
name|pos
operator|<
name|len
condition|)
block|{
specifier|final
name|int
name|consumed
init|=
name|translate
argument_list|(
name|input
argument_list|,
name|pos
argument_list|,
name|out
argument_list|)
decl_stmt|;
if|if
condition|(
name|consumed
operator|==
literal|0
condition|)
block|{
comment|// inlined implementation of Character.toChars(Character.codePointAt(input, pos))
comment|// avoids allocating temp char arrays and duplicate checks
specifier|final
name|char
name|c1
init|=
name|input
operator|.
name|charAt
argument_list|(
name|pos
argument_list|)
decl_stmt|;
name|out
operator|.
name|write
argument_list|(
name|c1
argument_list|)
expr_stmt|;
name|pos
operator|++
expr_stmt|;
if|if
condition|(
name|Character
operator|.
name|isHighSurrogate
argument_list|(
name|c1
argument_list|)
operator|&&
name|pos
operator|<
name|len
condition|)
block|{
specifier|final
name|char
name|c2
init|=
name|input
operator|.
name|charAt
argument_list|(
name|pos
argument_list|)
decl_stmt|;
if|if
condition|(
name|Character
operator|.
name|isLowSurrogate
argument_list|(
name|c2
argument_list|)
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|c2
argument_list|)
expr_stmt|;
name|pos
operator|++
expr_stmt|;
block|}
block|}
continue|continue;
block|}
comment|// contract with translators is that they have to understand codepoints
comment|// and they just took care of a surrogate pair
for|for
control|(
name|int
name|pt
init|=
literal|0
init|;
name|pt
operator|<
name|consumed
condition|;
name|pt
operator|++
control|)
block|{
name|pos
operator|+=
name|Character
operator|.
name|charCount
argument_list|(
name|Character
operator|.
name|codePointAt
argument_list|(
name|input
argument_list|,
name|pos
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Helper method to create a merger of this translator with another set of      * translators. Useful in customizing the standard functionality.      *      * @param translators CharSequenceTranslator array of translators to merge with this one      * @return CharSequenceTranslator merging this translator with the others      */
specifier|public
specifier|final
name|CharSequenceTranslator
name|with
parameter_list|(
specifier|final
name|CharSequenceTranslator
modifier|...
name|translators
parameter_list|)
block|{
specifier|final
name|CharSequenceTranslator
index|[]
name|newArray
init|=
operator|new
name|CharSequenceTranslator
index|[
name|translators
operator|.
name|length
operator|+
literal|1
index|]
decl_stmt|;
name|newArray
index|[
literal|0
index|]
operator|=
name|this
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|translators
argument_list|,
literal|0
argument_list|,
name|newArray
argument_list|,
literal|1
argument_list|,
name|translators
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
operator|new
name|AggregateTranslator
argument_list|(
name|newArray
argument_list|)
return|;
block|}
comment|/**      *<p>Returns an upper case hexadecimal {@code String} for the given      * character.</p>      *      * @param codepoint The codepoint to convert.      * @return An upper case hexadecimal {@code String}      */
specifier|public
specifier|static
name|String
name|hex
parameter_list|(
specifier|final
name|int
name|codepoint
parameter_list|)
block|{
return|return
name|Integer
operator|.
name|toHexString
argument_list|(
name|codepoint
argument_list|)
operator|.
name|toUpperCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
return|;
block|}
block|}
end_class

end_unit
