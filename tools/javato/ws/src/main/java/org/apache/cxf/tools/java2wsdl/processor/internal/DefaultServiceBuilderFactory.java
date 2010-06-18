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
name|java2wsdl
operator|.
name|processor
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|aegis
operator|.
name|databinding
operator|.
name|AegisDatabinding
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
name|databinding
operator|.
name|DataBinding
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
name|frontend
operator|.
name|AbstractServiceFactory
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
name|JAXBDataBinding
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
name|jaxws
operator|.
name|JaxwsServiceBuilder
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
name|service
operator|.
name|ServiceBuilder
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
name|simple
operator|.
name|SimpleServiceBuilder
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
name|tools
operator|.
name|java2wsdl
operator|.
name|processor
operator|.
name|FrontendFactory
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
name|java2wsdl
operator|.
name|processor
operator|.
name|FrontendFactory
operator|.
name|Style
import|;
end_import

begin_comment
comment|/**  * This class constructs ServiceBuilder objects. These objects are used to access the services and the data  * bindings to generate the wsdl.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|DefaultServiceBuilderFactory
extends|extends
name|ServiceBuilderFactory
block|{
annotation|@
name|Override
specifier|public
name|ServiceBuilder
name|newBuilder
parameter_list|(
name|FrontendFactory
operator|.
name|Style
name|s
parameter_list|)
block|{
name|DataBinding
name|dataBinding
decl_stmt|;
specifier|final
name|String
name|dbn
init|=
name|getDatabindingName
argument_list|()
decl_stmt|;
if|if
condition|(
name|ToolConstants
operator|.
name|JAXB_DATABINDING
operator|.
name|equals
argument_list|(
name|dbn
argument_list|)
condition|)
block|{
name|dataBinding
operator|=
operator|new
name|JAXBDataBinding
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ToolConstants
operator|.
name|AEGIS_DATABINDING
operator|.
name|equals
argument_list|(
name|dbn
argument_list|)
condition|)
block|{
name|dataBinding
operator|=
operator|new
name|AegisDatabinding
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|ToolException
argument_list|(
literal|"Unsupported databinding: "
operator|+
name|s
argument_list|)
throw|;
block|}
name|AbstractServiceFactory
name|builder
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|Style
operator|.
name|Jaxws
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|builder
operator|=
operator|new
name|JaxwsServiceBuilder
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Style
operator|.
name|Simple
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|builder
operator|=
operator|new
name|SimpleServiceBuilder
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|ToolException
argument_list|(
literal|"Unsupported frontend style: "
operator|+
name|s
argument_list|)
throw|;
block|}
name|builder
operator|.
name|setDataBinding
argument_list|(
name|dataBinding
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setServiceClass
argument_list|(
name|serviceClass
argument_list|)
expr_stmt|;
return|return
name|builder
return|;
block|}
block|}
end_class

end_unit

