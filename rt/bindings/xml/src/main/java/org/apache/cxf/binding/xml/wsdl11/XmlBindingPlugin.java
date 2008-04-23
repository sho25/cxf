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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

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
name|WSDLException
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
name|bindings
operator|.
name|xformat
operator|.
name|XMLFormatBinding
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
name|ToolConstants
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
name|AbstractWSDLPlugin
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|XmlBindingPlugin
extends|extends
name|AbstractWSDLPlugin
block|{
specifier|public
name|ExtensibilityElement
name|createExtension
parameter_list|(
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|args
parameter_list|)
throws|throws
name|WSDLException
block|{
name|XMLFormatBinding
name|xmlBinding
init|=
literal|null
decl_stmt|;
name|xmlBinding
operator|=
operator|(
name|XMLFormatBinding
operator|)
name|registry
operator|.
name|createExtension
argument_list|(
name|Binding
operator|.
name|class
argument_list|,
name|ToolConstants
operator|.
name|XML_BINDING_FORMAT
argument_list|)
expr_stmt|;
return|return
name|xmlBinding
return|;
block|}
block|}
end_class

end_unit

