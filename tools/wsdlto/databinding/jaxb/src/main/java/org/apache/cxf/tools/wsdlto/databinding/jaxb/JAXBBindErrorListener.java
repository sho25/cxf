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
name|wsdlto
operator|.
name|databinding
operator|.
name|jaxb
package|;
end_package

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
name|api
operator|.
name|ErrorListener
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
name|ToolErrorListener
import|;
end_import

begin_class
specifier|public
class|class
name|JAXBBindErrorListener
implements|implements
name|ErrorListener
block|{
specifier|private
name|boolean
name|isVerbose
decl_stmt|;
specifier|private
name|ToolErrorListener
name|listener
decl_stmt|;
specifier|public
name|JAXBBindErrorListener
parameter_list|(
name|boolean
name|verbose
parameter_list|,
name|ToolErrorListener
name|l
parameter_list|)
block|{
name|isVerbose
operator|=
name|verbose
expr_stmt|;
name|listener
operator|=
name|l
expr_stmt|;
block|}
specifier|public
name|boolean
name|hasErrors
parameter_list|()
block|{
return|return
name|listener
operator|.
name|getErrorCount
argument_list|()
operator|>
literal|0
return|;
block|}
specifier|public
name|void
name|error
parameter_list|(
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXParseException
name|exception
parameter_list|)
block|{
name|listener
operator|.
name|addError
argument_list|(
name|exception
operator|.
name|getSystemId
argument_list|()
argument_list|,
name|exception
operator|.
name|getLineNumber
argument_list|()
argument_list|,
name|exception
operator|.
name|getColumnNumber
argument_list|()
argument_list|,
name|mapMessage
argument_list|(
name|exception
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|fatalError
parameter_list|(
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXParseException
name|exception
parameter_list|)
block|{
name|error
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|info
parameter_list|(
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXParseException
name|exception
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|isVerbose
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"JAXB Info: "
operator|+
name|exception
operator|.
name|toString
argument_list|()
operator|+
literal|" in schema "
operator|+
name|exception
operator|.
name|getSystemId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|warning
parameter_list|(
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXParseException
name|exception
parameter_list|)
block|{
for|for
control|(
name|StackTraceElement
name|el
range|:
operator|new
name|Exception
argument_list|()
operator|.
name|getStackTrace
argument_list|()
control|)
block|{
if|if
condition|(
name|el
operator|.
name|getClassName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"DowngradingErrorHandler"
argument_list|)
condition|)
block|{
comment|//this is from within JAXB as it tries to validate the schemas
comment|//Xerces has issues with schema imports that don't have a
comment|//schemaLocation (or a schemaLocation that is not fully resolvable)
comment|//and emits strange warnings that are completely not
comment|//correct so we'll try and skip them.
return|return;
block|}
block|}
if|if
condition|(
name|this
operator|.
name|isVerbose
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"JAXB parsing schema warning "
operator|+
name|exception
operator|.
name|toString
argument_list|()
operator|+
literal|" in schema "
operator|+
name|exception
operator|.
name|getSystemId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|listener
operator|.
name|addWarning
argument_list|(
name|exception
operator|.
name|getSystemId
argument_list|()
argument_list|,
name|exception
operator|.
name|getLineNumber
argument_list|()
argument_list|,
name|exception
operator|.
name|getColumnNumber
argument_list|()
argument_list|,
name|mapMessage
argument_list|(
name|exception
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|mapMessage
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
comment|//this is kind of a hack to map the JAXB error message into
comment|//something more appropriate for CXF.  If JAXB changes their
comment|//error messages, this will break
if|if
condition|(
name|msg
operator|.
name|contains
argument_list|(
literal|"Use a class customization to resolve"
argument_list|)
operator|&&
name|msg
operator|.
name|contains
argument_list|(
literal|"with the same name"
argument_list|)
condition|)
block|{
name|int
name|idx
init|=
name|msg
operator|.
name|lastIndexOf
argument_list|(
literal|"class customization"
argument_list|)
operator|+
literal|19
decl_stmt|;
name|msg
operator|=
name|msg
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
operator|+
literal|" or the -autoNameResolution option"
operator|+
name|msg
operator|.
name|substring
argument_list|(
name|idx
argument_list|)
expr_stmt|;
block|}
return|return
name|msg
return|;
block|}
block|}
end_class

end_unit

