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
name|core
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Definition
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
name|tools
operator|.
name|common
operator|.
name|ToolContext
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
name|WSDLBuilder
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractWSDLBuilder
implements|implements
name|WSDLBuilder
argument_list|<
name|Definition
argument_list|>
block|{
specifier|protected
name|ToolContext
name|context
decl_stmt|;
specifier|protected
name|Bus
name|bus
decl_stmt|;
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|this
operator|.
name|bus
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|void
name|setContext
parameter_list|(
name|ToolContext
name|c
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|c
expr_stmt|;
block|}
specifier|public
specifier|abstract
name|void
name|customize
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|boolean
name|validate
parameter_list|(
name|Definition
name|t
parameter_list|)
throws|throws
name|ToolException
function_decl|;
specifier|public
specifier|abstract
name|Definition
name|getWSDLModel
parameter_list|()
function_decl|;
block|}
end_class

end_unit

