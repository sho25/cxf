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

begin_comment
comment|/**  *<p>Escapes and unescapes {@code String}s for  * Java, Java Script, HTML and XML.</p>  *  *<p>#ThreadSafe#</p>  *  *  *<p>  * This code has been adapted from Apache Commons Lang 3.5.  *</p>  *  * @since 1.0  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|StringEscapeUtils
block|{
comment|/* ESCAPE TRANSLATORS */
comment|/**      * Translator object for escaping HTML version 4.0.      *      * While {@link #escapeHtml4(String)} is the expected method of use, this      * object allows the HTML escaping functionality to be used      * as the foundation for a custom translator.      */
specifier|public
specifier|static
specifier|final
name|CharSequenceTranslator
name|ESCAPE_HTML4
init|=
operator|new
name|AggregateTranslator
argument_list|(
operator|new
name|LookupTranslator
argument_list|(
name|EntityArrays
operator|.
name|BASIC_ESCAPE
argument_list|)
argument_list|,
operator|new
name|LookupTranslator
argument_list|(
name|EntityArrays
operator|.
name|ISO8859_1_ESCAPE
argument_list|)
argument_list|,
operator|new
name|LookupTranslator
argument_list|(
name|EntityArrays
operator|.
name|HTML40_EXTENDED_ESCAPE
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
name|StringEscapeUtils
parameter_list|()
block|{
comment|// complete
block|}
comment|// HTML and XML
comment|//--------------------------------------------------------------------------
comment|/**      *<p>Escapes the characters in a {@code String} using HTML entities.</p>      *      *<p>      * For example:      *</p>      *<p>{@code "bread"&amp; "butter"}</p>      * becomes:      *<p>      * {@code&amp;quot;bread&amp;quot;&amp;amp;&amp;quot;butter&amp;quot;}.      *</p>      *      *<p>Supports all known HTML 4.0 entities, including funky accents.      * Note that the commonly used apostrophe escape character (&amp;apos;)      * is not a legal entity and so is not supported).</p>      *      * @param input  the {@code String} to escape, may be null      * @return a new escaped {@code String}, {@code null} if null string input      *      * @see<a href="http://hotwired.lycos.com/webmonkey/reference/special_characters/">ISO Entities</a>      * @see<a href="http://www.w3.org/TR/REC-html32#latin1">HTML 3.2 Character Entities for ISO Latin-1</a>      * @see<a href="http://www.w3.org/TR/REC-html40/sgml/entities.html">HTML 4.0 Character entity references</a>      * @see<a href="http://www.w3.org/TR/html401/charset.html#h-5.3">HTML 4.01 Character References</a>      * @see<a href="http://www.w3.org/TR/html401/charset.html#code-position">HTML 4.01 Code positions</a>      */
specifier|public
specifier|static
name|String
name|escapeHtml4
parameter_list|(
specifier|final
name|String
name|input
parameter_list|)
block|{
return|return
name|ESCAPE_HTML4
operator|.
name|translate
argument_list|(
name|input
argument_list|)
return|;
block|}
block|}
end_class

end_unit

