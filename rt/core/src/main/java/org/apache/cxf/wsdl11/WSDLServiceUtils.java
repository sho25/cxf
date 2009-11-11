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
name|wsdl11
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Binding
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|ExtensibilityElement
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
name|Bus
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
name|BusException
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
name|binding
operator|.
name|BindingFactory
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
name|binding
operator|.
name|BindingFactoryManager
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|WSDLServiceUtils
block|{
specifier|private
name|WSDLServiceUtils
parameter_list|()
block|{      }
specifier|public
specifier|static
name|BindingFactory
name|getBindingFactory
parameter_list|(
name|Binding
name|binding
parameter_list|,
name|Bus
name|bus
parameter_list|,
name|StringBuilder
name|sb
parameter_list|)
block|{
name|BindingFactory
name|factory
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Object
name|obj
range|:
name|binding
operator|.
name|getExtensibilityElements
argument_list|()
control|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|ExtensibilityElement
condition|)
block|{
name|ExtensibilityElement
name|ext
init|=
operator|(
name|ExtensibilityElement
operator|)
name|obj
decl_stmt|;
name|sb
operator|.
name|delete
argument_list|(
literal|0
argument_list|,
name|sb
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|ext
operator|.
name|getElementType
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|factory
operator|=
name|bus
operator|.
name|getExtension
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|)
operator|.
name|getBindingFactory
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BusException
name|e
parameter_list|)
block|{
comment|// ignore, we'll use a generic BindingInfo
block|}
if|if
condition|(
name|factory
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
block|}
block|}
return|return
name|factory
return|;
block|}
block|}
end_class

end_unit

