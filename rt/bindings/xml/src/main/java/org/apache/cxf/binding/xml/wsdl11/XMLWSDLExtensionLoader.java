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
name|binding
operator|.
name|xml
operator|.
name|wsdl11
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
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
name|common
operator|.
name|injection
operator|.
name|NoJSR250Annotations
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
name|wsdl
operator|.
name|JAXBExtensionHelper
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
name|wsdl
operator|.
name|WSDLExtensionLoader
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
name|wsdl
operator|.
name|WSDLManager
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
annotation|@
name|NoJSR250Annotations
specifier|public
specifier|final
class|class
name|XMLWSDLExtensionLoader
implements|implements
name|WSDLExtensionLoader
block|{
specifier|public
name|XMLWSDLExtensionLoader
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|setupBus
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setupBus
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|WSDLManager
name|manager
init|=
name|b
operator|.
name|getExtension
argument_list|(
name|WSDLManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|registerExtensors
argument_list|(
name|manager
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|registerExtensors
parameter_list|(
name|WSDLManager
name|manager
parameter_list|)
block|{
name|createExtensor
argument_list|(
name|manager
argument_list|,
name|javax
operator|.
name|wsdl
operator|.
name|BindingInput
operator|.
name|class
argument_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|bindings
operator|.
name|xformat
operator|.
name|XMLBindingMessageFormat
operator|.
name|class
argument_list|)
expr_stmt|;
name|createExtensor
argument_list|(
name|manager
argument_list|,
name|javax
operator|.
name|wsdl
operator|.
name|BindingOutput
operator|.
name|class
argument_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|bindings
operator|.
name|xformat
operator|.
name|XMLBindingMessageFormat
operator|.
name|class
argument_list|)
expr_stmt|;
name|createExtensor
argument_list|(
name|manager
argument_list|,
name|javax
operator|.
name|wsdl
operator|.
name|Binding
operator|.
name|class
argument_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|bindings
operator|.
name|xformat
operator|.
name|XMLFormatBinding
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|createExtensor
parameter_list|(
name|WSDLManager
name|manager
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|parentType
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|elementType
parameter_list|)
block|{
try|try
block|{
name|JAXBExtensionHelper
operator|.
name|addExtensions
argument_list|(
name|manager
operator|.
name|getExtensionRegistry
argument_list|()
argument_list|,
name|parentType
argument_list|,
name|elementType
argument_list|,
literal|null
argument_list|,
name|XMLWSDLExtensionLoader
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
comment|//ignore, won't support XML
block|}
block|}
block|}
end_class

end_unit

