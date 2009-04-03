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
name|ToolException
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
name|String
name|prefix
init|=
literal|"Thrown by JAXB : "
decl_stmt|;
specifier|public
name|JAXBBindErrorListener
parameter_list|(
name|boolean
name|verbose
parameter_list|)
block|{
name|isVerbose
operator|=
name|verbose
expr_stmt|;
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
if|if
condition|(
name|exception
operator|.
name|getLineNumber
argument_list|()
operator|>
literal|0
condition|)
block|{
throw|throw
operator|new
name|ToolException
argument_list|(
name|prefix
operator|+
name|exception
operator|.
name|getLocalizedMessage
argument_list|()
operator|+
literal|" at line "
operator|+
name|exception
operator|.
name|getLineNumber
argument_list|()
operator|+
literal|" column "
operator|+
name|exception
operator|.
name|getColumnNumber
argument_list|()
operator|+
literal|" of schema "
operator|+
name|exception
operator|.
name|getSystemId
argument_list|()
argument_list|,
name|exception
argument_list|)
throw|;
block|}
throw|throw
operator|new
name|ToolException
argument_list|(
name|prefix
operator|+
name|exception
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|,
name|exception
argument_list|)
throw|;
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
throw|throw
operator|new
name|ToolException
argument_list|(
name|prefix
operator|+
name|exception
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|,
name|exception
argument_list|)
throw|;
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
if|if
condition|(
name|this
operator|.
name|isVerbose
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"JAXB parsing schema warning "
operator|+
name|exception
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

