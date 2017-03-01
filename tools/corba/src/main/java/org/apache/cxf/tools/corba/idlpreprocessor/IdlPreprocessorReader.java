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
name|corba
operator|.
name|idlpreprocessor
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
name|LineNumberReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Stack
import|;
end_import

begin_comment
comment|/**  * A Reader that implements the #include functionality of the preprocessor.  * Starting from one URL, it generates one stream of characters by tracking  * #defines, #ifdefs, etc. and following #includes accordingly.  *  *<p>  * This reader augments the stream with  *<a href="http://gcc.gnu.org/onlinedocs/gcc-3.2.3/cpp/Preprocessor-Output.html">  * location information</a> when the source URL is switched.  * This improves error reporting (with correct file and linenumber information) in the  * subsequent compilation steps like IDL parsing and also allows the implentation  * of code generation options like the -emitAll flag available in the JDK idlj tool.  *</p>  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|IdlPreprocessorReader
extends|extends
name|Reader
block|{
comment|/**      * Maximum depth of {@link #includeStack} to prevent infinite recursion.      */
specifier|private
specifier|static
specifier|final
name|int
name|MAX_INCLUDE_DEPTH
init|=
literal|64
decl_stmt|;
comment|/**      * GNU standard preprocessor output flag for signalling a new file.      *      * @see http://gcc.gnu.org/onlinedocs/gcc-3.2.3/cpp/Preprocessor-Output.html      */
specifier|private
specifier|static
specifier|final
name|char
name|PUSH
init|=
literal|'1'
decl_stmt|;
comment|/**      * GNU standard preprocessor output flag for signalling returning to a file.      *      * @see http://gcc.gnu.org/onlinedocs/gcc-3.2.3/cpp/Preprocessor-Output.html      */
specifier|private
specifier|static
specifier|final
name|char
name|POP
init|=
literal|'2'
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|LF
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|IncludeResolver
name|includeResolver
decl_stmt|;
specifier|private
specifier|final
name|Stack
argument_list|<
name|IncludeStackEntry
argument_list|>
name|includeStack
init|=
operator|new
name|Stack
argument_list|<
name|IncludeStackEntry
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Stack of Booleans, corresponding to nested 'if' preprocessor directives.      * The top of the stack signals whether the current idl code is skipped.      *      * @see #skips()      */
specifier|private
specifier|final
name|Stack
argument_list|<
name|Boolean
argument_list|>
name|ifStack
init|=
operator|new
name|Stack
argument_list|<
name|Boolean
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|DefineState
name|defineState
decl_stmt|;
specifier|private
specifier|final
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
specifier|private
name|int
name|readPos
decl_stmt|;
specifier|private
name|String
name|pragmaPrefix
decl_stmt|;
comment|/**      * Creates a new IncludeReader.      *      * @param startURL      * @param startLocation      * @param includeResolver      * @param defineState      * @throws IOException      */
specifier|public
name|IdlPreprocessorReader
parameter_list|(
name|URL
name|startURL
parameter_list|,
name|String
name|startLocation
parameter_list|,
name|IncludeResolver
name|resolver
parameter_list|,
name|DefineState
name|state
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|includeResolver
operator|=
name|resolver
expr_stmt|;
name|this
operator|.
name|defineState
operator|=
name|state
expr_stmt|;
name|pushInclude
argument_list|(
name|startURL
argument_list|,
name|startLocation
argument_list|)
expr_stmt|;
name|fillBuffer
argument_list|()
expr_stmt|;
block|}
comment|/**      * @param url      * @throws IOException      */
specifier|private
name|void
name|pushInclude
parameter_list|(
name|URL
name|url
parameter_list|,
name|String
name|location
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|IncludeStackEntry
name|includeStackEntry
init|=
operator|new
name|IncludeStackEntry
argument_list|(
name|url
argument_list|,
name|location
argument_list|)
decl_stmt|;
name|includeStack
operator|.
name|push
argument_list|(
name|includeStackEntry
argument_list|)
expr_stmt|;
specifier|final
name|int
name|lineNumber
init|=
name|getReader
argument_list|()
operator|.
name|getLineNumber
argument_list|()
decl_stmt|;
name|signalFileChange
argument_list|(
name|location
argument_list|,
name|lineNumber
argument_list|,
name|PUSH
argument_list|)
expr_stmt|;
block|}
comment|/**      * @see Reader#close()      */
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|buf
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
comment|/**      * @see Reader#read(char[], int, int)      */
specifier|public
name|int
name|read
parameter_list|(
name|char
index|[]
name|cbuf
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|int
name|buflen
init|=
name|buf
operator|.
name|length
argument_list|()
decl_stmt|;
if|if
condition|(
name|readPos
operator|>=
name|buflen
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|int
name|numCharsRead
init|=
name|Math
operator|.
name|min
argument_list|(
name|len
argument_list|,
name|buflen
operator|-
name|readPos
argument_list|)
decl_stmt|;
name|buf
operator|.
name|getChars
argument_list|(
name|readPos
argument_list|,
name|readPos
operator|+
name|numCharsRead
argument_list|,
name|cbuf
argument_list|,
name|off
argument_list|)
expr_stmt|;
name|readPos
operator|+=
name|numCharsRead
expr_stmt|;
return|return
name|numCharsRead
return|;
block|}
comment|/**      * @see Reader#read()      */
specifier|public
name|int
name|read
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|buf
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
else|else
block|{
return|return
name|buf
operator|.
name|charAt
argument_list|(
name|readPos
operator|++
argument_list|)
return|;
block|}
block|}
specifier|private
name|void
name|fillBuffer
parameter_list|()
throws|throws
name|IOException
block|{
while|while
condition|(
operator|!
name|includeStack
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LineNumberReader
name|reader
init|=
name|getReader
argument_list|()
decl_stmt|;
specifier|final
name|int
name|lineNo
init|=
name|reader
operator|.
name|getLineNumber
argument_list|()
decl_stmt|;
name|String
name|line
init|=
name|reader
operator|.
name|readLine
argument_list|()
decl_stmt|;
if|if
condition|(
name|line
operator|==
literal|null
condition|)
block|{
name|popInclude
argument_list|()
expr_stmt|;
continue|continue;
block|}
name|line
operator|=
name|processComments
argument_list|(
name|line
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|line
operator|.
name|trim
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|skips
argument_list|()
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
name|LF
argument_list|)
expr_stmt|;
continue|continue;
block|}
specifier|final
name|IncludeStackEntry
name|ise
init|=
name|includeStack
operator|.
name|peek
argument_list|()
decl_stmt|;
name|line
operator|=
name|line
operator|.
name|trim
argument_list|()
expr_stmt|;
name|line
operator|=
name|processPreprocessorComments
argument_list|(
name|buf
argument_list|,
name|line
argument_list|)
expr_stmt|;
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"#include"
argument_list|)
condition|)
block|{
name|handleInclude
argument_list|(
name|line
argument_list|,
name|lineNo
argument_list|,
name|ise
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"#ifndef"
argument_list|)
condition|)
block|{
name|handleIfndef
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"#ifdef"
argument_list|)
condition|)
block|{
name|handleIfdef
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"#if"
argument_list|)
condition|)
block|{
name|handleIf
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"#endif"
argument_list|)
condition|)
block|{
name|handleEndif
argument_list|(
name|lineNo
argument_list|,
name|ise
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"#else"
argument_list|)
condition|)
block|{
name|handleElse
argument_list|(
name|lineNo
argument_list|,
name|ise
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"#define"
argument_list|)
condition|)
block|{
name|handleDefine
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"#pragma"
argument_list|)
condition|)
block|{
name|handlePragma
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|PreprocessingException
argument_list|(
literal|"unknown preprocessor instruction"
argument_list|,
name|ise
operator|.
name|getURL
argument_list|()
argument_list|,
name|lineNo
argument_list|)
throw|;
block|}
block|}
block|}
specifier|private
name|String
name|processComments
parameter_list|(
name|String
name|line
parameter_list|)
block|{
name|int
name|pos
init|=
name|line
operator|.
name|indexOf
argument_list|(
literal|"**/"
argument_list|)
decl_stmt|;
comment|//The comments need to be end with */, so if the line has ****/,
comment|//we need to insert space to make it *** */
if|if
condition|(
name|pos
operator|!=
operator|-
literal|1
condition|)
block|{
name|line
operator|=
name|line
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
operator|+
literal|1
argument_list|)
operator|+
literal|" "
operator|+
name|line
operator|.
name|substring
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|line
return|;
block|}
specifier|private
name|String
name|processPreprocessorComments
parameter_list|(
name|StringBuilder
name|buffer
parameter_list|,
name|String
name|line
parameter_list|)
block|{
name|int
name|pos
init|=
name|line
operator|.
name|indexOf
argument_list|(
literal|"//"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|pos
operator|!=
operator|-
literal|1
operator|)
operator|&&
operator|(
name|pos
operator|!=
literal|0
operator|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|line
operator|.
name|substring
argument_list|(
name|pos
argument_list|)
argument_list|)
expr_stmt|;
name|line
operator|=
name|line
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
expr_stmt|;
block|}
name|pos
operator|=
name|line
operator|.
name|indexOf
argument_list|(
literal|"/*"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|(
name|pos
operator|!=
operator|-
literal|1
operator|)
operator|&&
operator|(
name|pos
operator|!=
literal|0
operator|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|line
operator|.
name|substring
argument_list|(
name|pos
argument_list|)
argument_list|)
expr_stmt|;
name|line
operator|=
name|line
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
expr_stmt|;
block|}
return|return
name|line
return|;
block|}
comment|/**      * TODO: support multiline definitions, functions, etc.      */
specifier|private
name|void
name|handleDefine
parameter_list|(
name|String
name|line
parameter_list|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|LF
argument_list|)
expr_stmt|;
if|if
condition|(
name|skips
argument_list|()
condition|)
block|{
return|return;
block|}
name|String
name|def
init|=
name|line
operator|.
name|substring
argument_list|(
literal|"#define"
operator|.
name|length
argument_list|()
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
name|int
name|idx
init|=
name|def
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|==
operator|-
literal|1
condition|)
block|{
name|defineState
operator|.
name|define
argument_list|(
name|def
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|symbol
init|=
name|def
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|def
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
name|defineState
operator|.
name|define
argument_list|(
name|symbol
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|handleElse
parameter_list|(
name|int
name|lineNo
parameter_list|,
specifier|final
name|IncludeStackEntry
name|ise
parameter_list|)
block|{
if|if
condition|(
name|ifStack
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|PreprocessingException
argument_list|(
literal|"unexpected #else"
argument_list|,
name|ise
operator|.
name|getURL
argument_list|()
argument_list|,
name|lineNo
argument_list|)
throw|;
block|}
name|boolean
name|top
init|=
name|ifStack
operator|.
name|pop
argument_list|()
decl_stmt|;
name|ifStack
operator|.
name|push
argument_list|(
operator|!
name|top
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|LF
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|handleEndif
parameter_list|(
name|int
name|lineNo
parameter_list|,
specifier|final
name|IncludeStackEntry
name|ise
parameter_list|)
block|{
if|if
condition|(
name|ifStack
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|PreprocessingException
argument_list|(
literal|"unexpected #endif"
argument_list|,
name|ise
operator|.
name|getURL
argument_list|()
argument_list|,
name|lineNo
argument_list|)
throw|;
block|}
name|ifStack
operator|.
name|pop
argument_list|()
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|LF
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|handleIfdef
parameter_list|(
name|String
name|line
parameter_list|)
block|{
name|String
name|symbol
init|=
name|line
operator|.
name|substring
argument_list|(
literal|"#ifdef"
operator|.
name|length
argument_list|()
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
name|boolean
name|isDefined
init|=
name|defineState
operator|.
name|isDefined
argument_list|(
name|symbol
argument_list|)
decl_stmt|;
name|registerIf
argument_list|(
operator|!
name|isDefined
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|LF
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|handleIf
parameter_list|(
name|String
name|line
parameter_list|)
block|{
name|String
name|symbol
init|=
name|line
operator|.
name|substring
argument_list|(
literal|"#if"
operator|.
name|length
argument_list|()
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
name|boolean
name|notSkip
init|=
literal|true
decl_stmt|;
try|try
block|{
name|int
name|value
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|symbol
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|0
condition|)
block|{
name|notSkip
operator|=
literal|false
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
comment|//do nothig
block|}
name|registerIf
argument_list|(
operator|!
name|notSkip
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|LF
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|handlePragma
parameter_list|(
name|String
name|line
parameter_list|)
block|{
name|String
name|symbol
init|=
name|line
operator|.
name|substring
argument_list|(
name|line
operator|.
name|indexOf
argument_list|(
literal|"prefix"
argument_list|)
operator|+
literal|"prefix"
operator|.
name|length
argument_list|()
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|symbol
operator|.
name|startsWith
argument_list|(
literal|"\""
argument_list|)
condition|)
block|{
name|symbol
operator|=
name|symbol
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|symbol
operator|.
name|endsWith
argument_list|(
literal|"\""
argument_list|)
condition|)
block|{
name|symbol
operator|=
name|symbol
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|symbol
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|setPragmaPrefix
argument_list|(
name|symbol
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|LF
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|handleIfndef
parameter_list|(
name|String
name|line
parameter_list|)
block|{
name|String
name|symbol
init|=
name|line
operator|.
name|substring
argument_list|(
literal|"#ifndef"
operator|.
name|length
argument_list|()
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
name|boolean
name|isDefined
init|=
name|defineState
operator|.
name|isDefined
argument_list|(
name|symbol
argument_list|)
decl_stmt|;
name|registerIf
argument_list|(
name|isDefined
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|LF
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|handleInclude
parameter_list|(
name|String
name|line
parameter_list|,
name|int
name|lineNo
parameter_list|,
specifier|final
name|IncludeStackEntry
name|ise
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|skips
argument_list|()
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|LF
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|includeStack
operator|.
name|size
argument_list|()
operator|>=
name|MAX_INCLUDE_DEPTH
condition|)
block|{
throw|throw
operator|new
name|PreprocessingException
argument_list|(
literal|"more than "
operator|+
name|MAX_INCLUDE_DEPTH
operator|+
literal|" nested #includes - assuming infinite recursion, aborting"
argument_list|,
name|ise
operator|.
name|getURL
argument_list|()
argument_list|,
name|lineNo
argument_list|)
throw|;
block|}
name|String
name|arg
init|=
name|line
operator|.
name|replaceFirst
argument_list|(
literal|"#include"
argument_list|,
literal|""
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|arg
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|PreprocessingException
argument_list|(
literal|"#include without an argument"
argument_list|,
name|ise
operator|.
name|getURL
argument_list|()
argument_list|,
name|lineNo
argument_list|)
throw|;
block|}
name|char
name|first
init|=
name|arg
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
decl_stmt|;
specifier|final
name|int
name|lastIdx
init|=
name|arg
operator|.
name|length
argument_list|()
operator|-
literal|1
decl_stmt|;
name|char
name|last
init|=
name|arg
operator|.
name|charAt
argument_list|(
name|lastIdx
argument_list|)
decl_stmt|;
if|if
condition|(
name|arg
operator|.
name|length
argument_list|()
operator|<
literal|3
operator|||
operator|!
operator|(
name|first
operator|==
literal|'<'
operator|&&
name|last
operator|==
literal|'>'
operator|)
operator|&&
operator|!
operator|(
name|first
operator|==
literal|'"'
operator|&&
name|last
operator|==
literal|'"'
operator|)
condition|)
block|{
throw|throw
operator|new
name|PreprocessingException
argument_list|(
literal|"argument for '#include' must be enclosed in '<>' or '\" \"'"
argument_list|,
name|ise
operator|.
name|getURL
argument_list|()
argument_list|,
name|lineNo
argument_list|)
throw|;
block|}
name|String
name|spec
init|=
name|arg
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|lastIdx
argument_list|)
decl_stmt|;
name|URL
name|include
init|=
operator|(
name|first
operator|==
literal|'<'
operator|)
condition|?
name|includeResolver
operator|.
name|findSystemInclude
argument_list|(
name|spec
argument_list|)
else|:
name|includeResolver
operator|.
name|findUserInclude
argument_list|(
name|spec
argument_list|)
decl_stmt|;
if|if
condition|(
name|include
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|PreprocessingException
argument_list|(
literal|"unable to resolve include '"
operator|+
name|spec
operator|+
literal|"'"
argument_list|,
name|ise
operator|.
name|getURL
argument_list|()
argument_list|,
name|lineNo
argument_list|)
throw|;
block|}
name|pushInclude
argument_list|(
name|include
argument_list|,
name|spec
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|popInclude
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|IncludeStackEntry
name|poppedStackEntry
init|=
name|includeStack
operator|.
name|pop
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|includeStack
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|LF
argument_list|)
expr_stmt|;
block|}
try|try
block|{
if|if
condition|(
operator|!
name|includeStack
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
specifier|final
name|IncludeStackEntry
name|newTopEntry
init|=
name|includeStack
operator|.
name|peek
argument_list|()
decl_stmt|;
specifier|final
name|LineNumberReader
name|reader
init|=
name|getReader
argument_list|()
decl_stmt|;
specifier|final
name|int
name|lineNumber
init|=
name|reader
operator|.
name|getLineNumber
argument_list|()
decl_stmt|;
specifier|final
name|String
name|location
init|=
name|newTopEntry
operator|.
name|getLocation
argument_list|()
decl_stmt|;
name|signalFileChange
argument_list|(
name|location
argument_list|,
name|lineNumber
argument_list|,
name|POP
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|poppedStackEntry
operator|.
name|getReader
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|skips
parameter_list|()
block|{
if|if
condition|(
name|ifStack
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|ifStack
operator|.
name|peek
argument_list|()
return|;
block|}
specifier|private
name|void
name|registerIf
parameter_list|(
name|boolean
name|skip
parameter_list|)
block|{
name|ifStack
operator|.
name|push
argument_list|(
name|skip
argument_list|)
expr_stmt|;
block|}
specifier|private
name|LineNumberReader
name|getReader
parameter_list|()
block|{
name|IncludeStackEntry
name|topOfStack
init|=
name|includeStack
operator|.
name|peek
argument_list|()
decl_stmt|;
return|return
name|topOfStack
operator|.
name|getReader
argument_list|()
return|;
block|}
comment|/**      * Creates GNU standard preprocessor flag for signalling a file change.      *      * @see http://gcc.gnu.org/onlinedocs/gcc-3.2.3/cpp/Preprocessor-Output.html      */
specifier|private
name|void
name|signalFileChange
parameter_list|(
name|String
name|location
parameter_list|,
name|int
name|lineNumber
parameter_list|,
name|char
name|flag
parameter_list|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"# "
argument_list|)
operator|.
name|append
argument_list|(
name|lineNumber
argument_list|)
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
operator|.
name|append
argument_list|(
name|location
argument_list|)
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
operator|.
name|append
argument_list|(
name|flag
argument_list|)
operator|.
name|append
argument_list|(
name|LF
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setPragmaPrefix
parameter_list|(
name|String
name|pragmaPrefix
parameter_list|)
block|{
name|this
operator|.
name|pragmaPrefix
operator|=
name|pragmaPrefix
expr_stmt|;
block|}
specifier|public
name|String
name|getPragmaPrefix
parameter_list|()
block|{
return|return
name|pragmaPrefix
return|;
block|}
block|}
end_class

end_unit

