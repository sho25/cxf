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
name|tools
operator|.
name|common
operator|.
name|toolspec
operator|.
name|parser
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringTokenizer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|XMLConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Transformer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamResult
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Attr
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|logging
operator|.
name|LogUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|DOMUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|common
operator|.
name|toolspec
operator|.
name|Tool
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|common
operator|.
name|toolspec
operator|.
name|ToolSpec
import|;
end_import

begin_class
specifier|public
class|class
name|CommandLineParser
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|CommandLineParser
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|ToolSpec
name|toolspec
decl_stmt|;
specifier|public
name|CommandLineParser
parameter_list|(
name|ToolSpec
name|ts
parameter_list|)
block|{
name|this
operator|.
name|toolspec
operator|=
name|ts
expr_stmt|;
block|}
specifier|public
name|void
name|setToolSpec
parameter_list|(
name|ToolSpec
name|ts
parameter_list|)
block|{
name|this
operator|.
name|toolspec
operator|=
name|ts
expr_stmt|;
block|}
specifier|public
specifier|static
name|String
index|[]
name|getArgsFromString
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|StringTokenizer
name|toker
init|=
operator|new
name|StringTokenizer
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|res
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
while|while
condition|(
name|toker
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|res
operator|.
name|add
argument_list|(
name|toker
operator|.
name|nextToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|res
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
return|;
block|}
specifier|public
name|CommandDocument
name|parseArguments
parameter_list|(
name|String
name|args
parameter_list|)
throws|throws
name|BadUsageException
throws|,
name|IOException
block|{
return|return
name|parseArguments
argument_list|(
name|getArgsFromString
argument_list|(
name|args
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|CommandDocument
name|parseArguments
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|BadUsageException
throws|,
name|IOException
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|StringBuilder
name|debugMsg
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"Parsing arguments: "
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|args
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|debugMsg
operator|.
name|append
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|fine
argument_list|(
name|debugMsg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|toolspec
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No schema known- call to acceptSchema() must be made and must succeed"
argument_list|)
throw|;
block|}
comment|// Create a result document
name|Document
name|resultDoc
init|=
literal|null
decl_stmt|;
try|try
block|{
name|DocumentBuilderFactory
name|factory
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setFeature
argument_list|(
name|XMLConstants
operator|.
name|FEATURE_SECURE_PROCESSING
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setFeature
argument_list|(
literal|"http://apache.org/xml/features/disallow-doctype-decl"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|resultDoc
operator|=
name|factory
operator|.
name|newDocumentBuilder
argument_list|()
operator|.
name|newDocument
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"FAIL_CREATE_DOM_MSG"
argument_list|)
expr_stmt|;
block|}
name|Element
name|commandEl
init|=
name|resultDoc
operator|.
name|createElementNS
argument_list|(
literal|"http://cxf.apache.org/Xutil/Command"
argument_list|,
literal|"command"
argument_list|)
decl_stmt|;
name|Attr
name|attr
init|=
name|commandEl
operator|.
name|getOwnerDocument
argument_list|()
operator|.
name|createAttributeNS
argument_list|(
literal|"http://www.w3.org/2001/XMLSchema-instance"
argument_list|,
literal|"xsi:schemaLocation"
argument_list|)
decl_stmt|;
name|attr
operator|.
name|setValue
argument_list|(
literal|"http://cxf.apache.org/Xutil/Command http://cxf.apache.org/schema/xutil/commnad.xsd"
argument_list|)
expr_stmt|;
name|commandEl
operator|.
name|setAttributeNodeNS
argument_list|(
name|attr
argument_list|)
expr_stmt|;
name|commandEl
operator|.
name|setAttribute
argument_list|(
literal|"xmlns"
argument_list|,
literal|"http://cxf.apache.org/Xutil/Command"
argument_list|)
expr_stmt|;
name|commandEl
operator|.
name|setAttribute
argument_list|(
literal|"xmlns:xsi"
argument_list|,
literal|"http://www.w3.org/2001/XMLSchema-instance"
argument_list|)
expr_stmt|;
name|resultDoc
operator|.
name|appendChild
argument_list|(
name|commandEl
argument_list|)
expr_stmt|;
name|TokenInputStream
name|tokens
init|=
operator|new
name|TokenInputStream
argument_list|(
name|args
argument_list|)
decl_stmt|;
comment|// for all form elements...
name|Element
name|usage
init|=
name|toolspec
operator|.
name|getUsage
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|usageForms
init|=
name|toolspec
operator|.
name|getUsageForms
argument_list|()
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Found "
operator|+
name|usageForms
operator|.
name|size
argument_list|()
operator|+
literal|" alternative forms of usage, will use default form"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|usageForms
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|ErrorVisitor
name|errors
init|=
operator|new
name|ErrorVisitor
argument_list|()
decl_stmt|;
for|for
control|(
name|Element
name|elem
range|:
name|usageForms
control|)
block|{
name|Form
name|form
init|=
operator|new
name|Form
argument_list|(
name|elem
argument_list|)
decl_stmt|;
name|int
name|pos
init|=
name|tokens
operator|.
name|getPosition
argument_list|()
decl_stmt|;
if|if
condition|(
name|form
operator|.
name|accept
argument_list|(
name|tokens
argument_list|,
name|commandEl
argument_list|,
name|errors
argument_list|)
condition|)
block|{
name|commandEl
operator|.
name|setAttribute
argument_list|(
literal|"form"
argument_list|,
name|form
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
comment|// if no more left then return null;
name|tokens
operator|.
name|setPosition
argument_list|(
name|pos
argument_list|)
expr_stmt|;
if|if
condition|(
name|elem
operator|.
name|getNextSibling
argument_list|()
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|INFO
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"No more forms left to try, returning null"
argument_list|)
expr_stmt|;
block|}
name|throwUsage
argument_list|(
name|errors
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*             for (int i = 0; i< usageForms.getLength(); i++) {                 Form form = new Form((Element)usageForms.item(i));                  int pos = tokens.getPosition();                  if (form.accept(tokens, commandEl, errors)) {                     commandEl.setAttribute("form", form.getName());                     break;                 } else {                     // if no more left then return null;                     tokens.setPosition(pos);                     if (i == usageForms.getLength() - 1) {                         if (LOG.isLoggable(Level.INFO)) {                             LOG.info("No more forms left to try, returning null");                         }                         throwUsage(errors);                     }                 }             } */
block|}
else|else
block|{
name|ErrorVisitor
name|errors
init|=
operator|new
name|ErrorVisitor
argument_list|()
decl_stmt|;
name|Form
name|form
init|=
operator|new
name|Form
argument_list|(
name|usage
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|form
operator|.
name|accept
argument_list|(
name|tokens
argument_list|,
name|commandEl
argument_list|,
name|errors
argument_list|)
condition|)
block|{
name|throwUsage
argument_list|(
name|errors
argument_list|)
expr_stmt|;
block|}
block|}
comment|// output the result document
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
try|try
block|{
name|TransformerFactory
name|transformerFactory
init|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|transformerFactory
operator|.
name|setFeature
argument_list|(
name|javax
operator|.
name|xml
operator|.
name|XMLConstants
operator|.
name|FEATURE_SECURE_PROCESSING
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Transformer
name|serializer
init|=
name|transformerFactory
operator|.
name|newTransformer
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|Tool
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"indent-no-xml-declaration.xsl"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
try|try
init|(
name|OutputStream
name|os
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
init|)
block|{
name|serializer
operator|.
name|transform
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|resultDoc
argument_list|)
argument_list|,
operator|new
name|StreamResult
argument_list|(
name|os
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
name|os
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"ERROR_SERIALIZE_COMMAND_MSG"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|CommandDocument
argument_list|(
name|toolspec
argument_list|,
name|resultDoc
argument_list|)
return|;
block|}
specifier|public
name|void
name|throwUsage
parameter_list|(
name|ErrorVisitor
name|errors
parameter_list|)
throws|throws
name|BadUsageException
throws|,
name|IOException
block|{
try|try
block|{
throw|throw
operator|new
name|BadUsageException
argument_list|(
name|getUsage
argument_list|()
argument_list|,
name|errors
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|TransformerException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"CANNOT_GET_USAGE_MSG"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|BadUsageException
argument_list|(
name|errors
argument_list|)
throw|;
block|}
block|}
specifier|public
name|String
name|getUsage
parameter_list|()
throws|throws
name|TransformerException
throws|,
name|IOException
block|{
comment|// REVISIT: style usage document into a form more readily output as a
comment|// usage message
try|try
init|(
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
init|;
name|InputStream
name|in
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"usage.xsl"
argument_list|)
init|)
block|{
name|toolspec
operator|.
name|transform
argument_list|(
name|in
argument_list|,
name|baos
argument_list|)
expr_stmt|;
return|return
name|baos
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
specifier|public
name|String
name|getDetailedUsage
parameter_list|()
throws|throws
name|TransformerException
throws|,
name|IOException
block|{
comment|// REVISIT: style usage document into a form more readily output as a
comment|// usage message
try|try
init|(
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
init|;
name|InputStream
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"detailedUsage.xsl"
argument_list|)
init|)
block|{
name|toolspec
operator|.
name|transform
argument_list|(
name|is
argument_list|,
name|baos
argument_list|)
expr_stmt|;
return|return
name|baos
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
specifier|public
name|String
name|getFormattedDetailedUsage
parameter_list|()
throws|throws
name|TransformerException
throws|,
name|IOException
block|{
name|String
name|usage
init|=
literal|null
decl_stmt|;
try|try
init|(
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
init|;
name|InputStream
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"detailedUsage.xsl"
argument_list|)
init|)
block|{
name|toolspec
operator|.
name|transform
argument_list|(
name|is
argument_list|,
name|baos
argument_list|)
expr_stmt|;
name|usage
operator|=
name|baos
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
comment|// we use the following pattern to format usage
comment|// |-------|-options|------|description-----------------|
comment|// before option white space size is 7
name|int
name|beforeOptSpan
init|=
literal|3
decl_stmt|;
comment|// option length is 8
name|int
name|optSize
init|=
literal|12
decl_stmt|;
comment|// after option white space size is 6
name|int
name|afterOptLen
init|=
literal|6
decl_stmt|;
name|int
name|totalLen
init|=
literal|80
decl_stmt|;
name|int
name|optSpan
init|=
name|optSize
operator|+
name|afterOptLen
operator|-
literal|1
decl_stmt|;
name|int
name|beforeDesSpan
init|=
name|beforeOptSpan
operator|+
name|optSpan
operator|+
literal|1
decl_stmt|;
name|String
name|lineSeparator
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
decl_stmt|;
name|StringTokenizer
name|st1
init|=
operator|new
name|StringTokenizer
argument_list|(
name|usage
argument_list|,
name|lineSeparator
argument_list|)
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
name|int
name|length
init|=
name|st1
operator|.
name|countTokens
argument_list|()
decl_stmt|;
name|String
index|[]
name|originalStrs
init|=
operator|new
name|String
index|[
name|length
index|]
decl_stmt|;
while|while
condition|(
name|st1
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|String
name|str
init|=
name|st1
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|originalStrs
index|[
name|i
index|]
operator|=
name|str
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
name|StringBuilder
name|strbuffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|length
operator|-
literal|1
condition|;
name|j
operator|=
name|j
operator|+
literal|2
control|)
block|{
name|int
name|optionLen
init|=
name|originalStrs
index|[
name|j
index|]
operator|.
name|length
argument_list|()
decl_stmt|;
name|addWhiteNamespace
argument_list|(
name|strbuffer
argument_list|,
name|beforeOptSpan
argument_list|)
expr_stmt|;
if|if
condition|(
name|optionLen
operator|<=
name|optSpan
condition|)
block|{
comment|//&& beforeOptSpan + optionLen + optSpan + desLen<= totalLen -
comment|// 1) {
name|strbuffer
operator|.
name|append
argument_list|(
name|originalStrs
index|[
name|j
index|]
argument_list|)
expr_stmt|;
name|addWhiteNamespace
argument_list|(
name|strbuffer
argument_list|,
name|optSpan
operator|-
name|originalStrs
index|[
name|j
index|]
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|strbuffer
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
if|if
condition|(
name|originalStrs
index|[
name|j
operator|+
literal|1
index|]
operator|.
name|length
argument_list|()
operator|>
name|totalLen
operator|-
name|beforeDesSpan
condition|)
block|{
name|int
name|lastIdx
init|=
name|totalLen
operator|-
name|beforeDesSpan
decl_stmt|;
name|int
name|lastIdx2
init|=
name|splitAndAppendText
argument_list|(
name|strbuffer
argument_list|,
name|originalStrs
index|[
name|j
operator|+
literal|1
index|]
argument_list|,
literal|0
argument_list|,
name|lastIdx
argument_list|)
decl_stmt|;
name|originalStrs
index|[
name|j
operator|+
literal|1
index|]
operator|=
name|originalStrs
index|[
name|j
operator|+
literal|1
index|]
operator|.
name|substring
argument_list|(
name|lastIdx2
argument_list|)
expr_stmt|;
name|strbuffer
operator|.
name|append
argument_list|(
name|lineSeparator
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|strbuffer
operator|.
name|append
argument_list|(
name|originalStrs
index|[
name|j
operator|+
literal|1
index|]
argument_list|)
expr_stmt|;
name|strbuffer
operator|.
name|append
argument_list|(
name|lineSeparator
argument_list|)
expr_stmt|;
name|originalStrs
index|[
name|j
operator|+
literal|1
index|]
operator|=
literal|""
expr_stmt|;
block|}
block|}
else|else
block|{
name|strbuffer
operator|.
name|append
argument_list|(
name|originalStrs
index|[
name|j
index|]
argument_list|)
expr_stmt|;
name|strbuffer
operator|.
name|append
argument_list|(
name|lineSeparator
argument_list|)
expr_stmt|;
block|}
name|String
name|tmpStr
init|=
name|originalStrs
index|[
name|j
operator|+
literal|1
index|]
decl_stmt|;
for|for
control|(
name|i
operator|=
literal|0
init|;
name|i
operator|<
name|tmpStr
operator|.
name|length
argument_list|()
condition|;
name|i
operator|=
name|i
operator|+
operator|(
name|totalLen
operator|-
name|beforeDesSpan
operator|)
control|)
block|{
if|if
condition|(
name|i
operator|+
name|totalLen
operator|-
name|beforeDesSpan
operator|<
name|tmpStr
operator|.
name|length
argument_list|()
condition|)
block|{
name|addWhiteNamespace
argument_list|(
name|strbuffer
argument_list|,
name|beforeDesSpan
argument_list|)
expr_stmt|;
name|int
name|lastIdx
init|=
name|i
operator|+
name|totalLen
operator|-
name|beforeDesSpan
decl_stmt|;
name|int
name|lastIdx2
init|=
name|splitAndAppendText
argument_list|(
name|strbuffer
argument_list|,
name|tmpStr
argument_list|,
name|i
argument_list|,
name|lastIdx
argument_list|)
decl_stmt|;
name|i
operator|+=
name|lastIdx2
operator|-
name|lastIdx
expr_stmt|;
name|strbuffer
operator|.
name|append
argument_list|(
name|lineSeparator
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|addWhiteNamespace
argument_list|(
name|strbuffer
argument_list|,
name|beforeDesSpan
argument_list|)
expr_stmt|;
name|strbuffer
operator|.
name|append
argument_list|(
name|tmpStr
operator|.
name|substring
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|strbuffer
operator|.
name|append
argument_list|(
name|lineSeparator
argument_list|)
expr_stmt|;
block|}
block|}
name|strbuffer
operator|.
name|append
argument_list|(
name|lineSeparator
argument_list|)
expr_stmt|;
block|}
return|return
name|strbuffer
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|int
name|splitAndAppendText
parameter_list|(
name|StringBuilder
name|buffer
parameter_list|,
name|String
name|tmpStr
parameter_list|,
name|int
name|idx
parameter_list|,
name|int
name|lastIdx
parameter_list|)
block|{
name|int
name|origLast
init|=
name|lastIdx
decl_stmt|;
while|while
condition|(
name|lastIdx
operator|>
name|idx
operator|&&
operator|!
name|Character
operator|.
name|isWhitespace
argument_list|(
name|tmpStr
operator|.
name|charAt
argument_list|(
name|lastIdx
argument_list|)
argument_list|)
condition|)
block|{
operator|--
name|lastIdx
expr_stmt|;
block|}
if|if
condition|(
name|lastIdx
operator|==
name|idx
condition|)
block|{
name|lastIdx
operator|=
name|origLast
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|tmpStr
operator|.
name|substring
argument_list|(
name|idx
argument_list|,
name|lastIdx
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|Character
operator|.
name|isWhitespace
argument_list|(
name|tmpStr
operator|.
name|charAt
argument_list|(
name|lastIdx
argument_list|)
argument_list|)
condition|)
block|{
name|lastIdx
operator|++
expr_stmt|;
block|}
return|return
name|lastIdx
return|;
block|}
specifier|private
name|void
name|addWhiteNamespace
parameter_list|(
name|StringBuilder
name|strbuffer
parameter_list|,
name|int
name|count
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|count
condition|;
name|i
operator|++
control|)
block|{
name|strbuffer
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getDetailedUsage
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|String
name|result
init|=
literal|null
decl_stmt|;
name|Element
name|element
init|=
name|toolspec
operator|.
name|getElementById
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|annotations
init|=
name|DOMUtils
operator|.
name|findAllElementsByTagNameNS
argument_list|(
name|element
argument_list|,
name|Tool
operator|.
name|TOOL_SPEC_PUBLIC_ID
argument_list|,
literal|"annotation"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|annotations
operator|!=
literal|null
operator|)
operator|&&
operator|(
operator|!
name|annotations
operator|.
name|isEmpty
argument_list|()
operator|)
condition|)
block|{
name|result
operator|=
name|annotations
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getFirstChild
argument_list|()
operator|.
name|getNodeValue
argument_list|()
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|public
name|String
name|getToolUsage
parameter_list|()
block|{
return|return
name|toolspec
operator|.
name|getAnnotation
argument_list|()
return|;
block|}
block|}
end_class

end_unit

