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
name|xjc
operator|.
name|ts
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
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|ErrorHandler
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|codemodel
operator|.
name|JClass
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|codemodel
operator|.
name|JDefinedClass
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|codemodel
operator|.
name|JDocComment
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|codemodel
operator|.
name|JExpr
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|codemodel
operator|.
name|JFieldRef
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|codemodel
operator|.
name|JInvocation
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|codemodel
operator|.
name|JMethod
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|codemodel
operator|.
name|JMod
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|tools
operator|.
name|xjc
operator|.
name|BadCommandLineException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|tools
operator|.
name|xjc
operator|.
name|Options
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|tools
operator|.
name|xjc
operator|.
name|outline
operator|.
name|ClassOutline
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|tools
operator|.
name|xjc
operator|.
name|outline
operator|.
name|Outline
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
name|jaxb
operator|.
name|JAXBToStringBuilder
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
name|jaxb
operator|.
name|JAXBToStringStyle
import|;
end_import

begin_comment
comment|/**  * Modifies the JAXB code model to override the Object.toString() method with an   * implementation that provides a String representation of the xml content.  */
end_comment

begin_class
specifier|public
class|class
name|ToStringPlugin
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
name|ToStringPlugin
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|String
name|styleFieldName
init|=
literal|"DEFAULT_STYLE"
decl_stmt|;
specifier|public
name|String
name|getOptionName
parameter_list|()
block|{
return|return
literal|"Xts"
return|;
block|}
specifier|public
name|String
name|getUsage
parameter_list|()
block|{
return|return
literal|"  -Xts                 : Activate plugin to add a toString() method to generated classes\n"
operator|+
literal|"  -Xts:style:multiline : Have toString produce multi line output\n"
operator|+
literal|"  -Xts:style:simple    : Have toString produce single line terse output\n"
return|;
block|}
specifier|public
name|int
name|parseArgument
parameter_list|(
name|Options
name|opt
parameter_list|,
name|String
index|[]
name|args
parameter_list|,
name|int
name|index
parameter_list|)
throws|throws
name|BadCommandLineException
throws|,
name|IOException
block|{
name|int
name|ret
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|args
index|[
name|index
index|]
operator|.
name|equals
argument_list|(
literal|"-Xts:style:multiline"
argument_list|)
condition|)
block|{
name|styleFieldName
operator|=
literal|"MULTI_LINE_STYLE"
expr_stmt|;
name|ret
operator|=
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|index
index|]
operator|.
name|equals
argument_list|(
literal|"-Xts:style:simple"
argument_list|)
condition|)
block|{
name|styleFieldName
operator|=
literal|"SIMPLE_STYLE"
expr_stmt|;
name|ret
operator|=
literal|1
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|public
name|boolean
name|run
parameter_list|(
name|Outline
name|outline
parameter_list|,
name|Options
name|opt
parameter_list|,
name|ErrorHandler
name|errorHandler
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Running toString() plugin."
argument_list|)
expr_stmt|;
specifier|final
name|JClass
name|toStringDelegateImpl
init|=
name|outline
operator|.
name|getCodeModel
argument_list|()
operator|.
name|ref
argument_list|(
name|JAXBToStringBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|JClass
name|styleClass
init|=
name|outline
operator|.
name|getCodeModel
argument_list|()
operator|.
name|ref
argument_list|(
name|JAXBToStringStyle
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|JFieldRef
name|toStringDelegateStyleParam
init|=
name|styleClass
operator|.
name|staticRef
argument_list|(
name|styleFieldName
argument_list|)
decl_stmt|;
for|for
control|(
name|ClassOutline
name|co
range|:
name|outline
operator|.
name|getClasses
argument_list|()
control|)
block|{
name|addToStringMethod
argument_list|(
name|co
argument_list|,
name|toStringDelegateImpl
argument_list|,
name|toStringDelegateStyleParam
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|void
name|addToStringMethod
parameter_list|(
name|ClassOutline
name|co
parameter_list|,
name|JClass
name|delegateImpl
parameter_list|,
name|JFieldRef
name|toStringDelegateStyleParam
parameter_list|)
block|{
specifier|final
name|JDefinedClass
name|implementation
init|=
name|co
operator|.
name|implClass
decl_stmt|;
specifier|final
name|JMethod
name|toStringMethod
init|=
name|implementation
operator|.
name|method
argument_list|(
name|JMod
operator|.
name|PUBLIC
argument_list|,
name|String
operator|.
name|class
argument_list|,
literal|"toString"
argument_list|)
decl_stmt|;
specifier|final
name|JInvocation
name|invoke
init|=
name|delegateImpl
operator|.
name|staticInvoke
argument_list|(
literal|"valueOf"
argument_list|)
decl_stmt|;
name|invoke
operator|.
name|arg
argument_list|(
name|JExpr
operator|.
name|_this
argument_list|()
argument_list|)
expr_stmt|;
name|invoke
operator|.
name|arg
argument_list|(
name|toStringDelegateStyleParam
argument_list|)
expr_stmt|;
name|toStringMethod
operator|.
name|body
argument_list|()
operator|.
name|_return
argument_list|(
name|invoke
argument_list|)
expr_stmt|;
name|JDocComment
name|doc
init|=
name|toStringMethod
operator|.
name|javadoc
argument_list|()
decl_stmt|;
name|doc
operator|.
name|add
argument_list|(
literal|"Generates a String representation of the contents of this type."
argument_list|)
expr_stmt|;
name|doc
operator|.
name|add
argument_list|(
literal|"\nThis is an extension method, produced by the 'ts' xjc plugin"
argument_list|)
expr_stmt|;
name|toStringMethod
operator|.
name|annotate
argument_list|(
name|Override
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

