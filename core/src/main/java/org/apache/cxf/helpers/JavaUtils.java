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
name|helpers
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|JavaUtils
block|{
comment|/** Use this character as suffix */
specifier|static
specifier|final
name|char
name|KEYWORD_PREFIX
init|=
literal|'_'
decl_stmt|;
comment|/**      * These are java keywords as specified at the following URL.      * http://java.sun.com/docs/books/jls/third_edition/html/lexical.html#3.9      * Note that false, true, and null are not strictly keywords; they are      * literal values, but for the purposes of this array, they can be treated      * as literals.      */
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|KEYWORDS
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"abstract"
argument_list|,
literal|"assert"
argument_list|,
literal|"boolean"
argument_list|,
literal|"break"
argument_list|,
literal|"byte"
argument_list|,
literal|"case"
argument_list|,
literal|"catch"
argument_list|,
literal|"char"
argument_list|,
literal|"class"
argument_list|,
literal|"const"
argument_list|,
literal|"continue"
argument_list|,
literal|"default"
argument_list|,
literal|"do"
argument_list|,
literal|"double"
argument_list|,
literal|"else"
argument_list|,
literal|"enum"
argument_list|,
literal|"extends"
argument_list|,
literal|"false"
argument_list|,
literal|"final"
argument_list|,
literal|"finally"
argument_list|,
literal|"float"
argument_list|,
literal|"for"
argument_list|,
literal|"goto"
argument_list|,
literal|"if"
argument_list|,
literal|"implements"
argument_list|,
literal|"import"
argument_list|,
literal|"instanceof"
argument_list|,
literal|"int"
argument_list|,
literal|"interface"
argument_list|,
literal|"long"
argument_list|,
literal|"native"
argument_list|,
literal|"new"
argument_list|,
literal|"null"
argument_list|,
literal|"package"
argument_list|,
literal|"private"
argument_list|,
literal|"protected"
argument_list|,
literal|"public"
argument_list|,
literal|"return"
argument_list|,
literal|"short"
argument_list|,
literal|"static"
argument_list|,
literal|"strictfp"
argument_list|,
literal|"super"
argument_list|,
literal|"switch"
argument_list|,
literal|"synchronized"
argument_list|,
literal|"this"
argument_list|,
literal|"throw"
argument_list|,
literal|"throws"
argument_list|,
literal|"transient"
argument_list|,
literal|"true"
argument_list|,
literal|"try"
argument_list|,
literal|"void"
argument_list|,
literal|"volatile"
argument_list|,
literal|"while"
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
name|JavaUtils
parameter_list|()
block|{     }
comment|/**      * checks if the input string is a valid java keyword.      *       * @return boolean true/false      */
specifier|public
specifier|static
name|boolean
name|isJavaKeyword
parameter_list|(
name|String
name|keyword
parameter_list|)
block|{
return|return
name|KEYWORDS
operator|.
name|contains
argument_list|(
name|keyword
argument_list|)
return|;
block|}
comment|/**      * Turn a java keyword string into a non-Java keyword string. (Right now      * this simply means appending an underscore.)      */
specifier|public
specifier|static
name|String
name|makeNonJavaKeyword
parameter_list|(
name|String
name|keyword
parameter_list|)
block|{
return|return
name|KEYWORD_PREFIX
operator|+
name|keyword
return|;
block|}
block|}
end_class

end_unit

