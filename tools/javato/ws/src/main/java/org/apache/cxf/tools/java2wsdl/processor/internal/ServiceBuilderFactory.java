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
name|java
operator|.
name|util
operator|.
name|List
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
name|java2wsdl
operator|.
name|processor
operator|.
name|FrontendFactory
import|;
end_import

begin_comment
comment|/**  * This class constructs ServiceBuilder objects. These objects are used to access the services and the data  * bindings to generate the wsdl.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|ServiceBuilderFactory
block|{
specifier|protected
name|FrontendFactory
name|frontend
decl_stmt|;
specifier|protected
name|String
name|databindingName
decl_stmt|;
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|serviceClass
decl_stmt|;
specifier|protected
name|ServiceBuilderFactory
parameter_list|()
block|{
name|frontend
operator|=
name|FrontendFactory
operator|.
name|getInstance
argument_list|()
expr_stmt|;
name|databindingName
operator|=
name|ToolConstants
operator|.
name|DEFAULT_DATA_BINDING_NAME
expr_stmt|;
block|}
specifier|public
specifier|static
name|ServiceBuilderFactory
name|getInstance
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|beanDefinitions
parameter_list|,
name|String
name|db
parameter_list|)
block|{
name|ServiceBuilderFactory
name|factory
decl_stmt|;
if|if
condition|(
name|beanDefinitions
operator|==
literal|null
operator|||
name|beanDefinitions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|ToolConstants
operator|.
name|JAXB_DATABINDING
operator|.
name|equals
argument_list|(
name|db
argument_list|)
operator|||
name|ToolConstants
operator|.
name|AEGIS_DATABINDING
operator|.
name|equals
argument_list|(
name|db
argument_list|)
condition|)
block|{
name|factory
operator|=
operator|new
name|DefaultServiceBuilderFactory
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|factory
operator|=
operator|new
name|SpringServiceBuilderFactory
argument_list|(
name|beanDefinitions
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|factory
operator|=
operator|new
name|SpringServiceBuilderFactory
argument_list|(
name|beanDefinitions
argument_list|)
expr_stmt|;
block|}
return|return
name|factory
return|;
block|}
specifier|public
name|ServiceBuilder
name|newBuilder
parameter_list|()
block|{
return|return
name|newBuilder
argument_list|(
name|getStyle
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|abstract
name|ServiceBuilder
name|newBuilder
parameter_list|(
name|FrontendFactory
operator|.
name|Style
name|s
parameter_list|)
function_decl|;
specifier|public
name|FrontendFactory
operator|.
name|Style
name|getStyle
parameter_list|()
block|{
name|frontend
operator|.
name|setServiceClass
argument_list|(
name|this
operator|.
name|serviceClass
argument_list|)
expr_stmt|;
return|return
name|frontend
operator|.
name|discoverStyle
argument_list|()
return|;
block|}
specifier|public
name|void
name|setServiceClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
name|this
operator|.
name|serviceClass
operator|=
name|c
expr_stmt|;
block|}
comment|/**      * Return the databinding name.      *      * @return      */
specifier|public
name|String
name|getDatabindingName
parameter_list|()
block|{
return|return
name|databindingName
return|;
block|}
comment|/**      * Set the databinding name      *      * @param databindingName      */
specifier|public
name|void
name|setDatabindingName
parameter_list|(
name|String
name|arg
parameter_list|)
block|{
name|databindingName
operator|=
name|arg
expr_stmt|;
block|}
block|}
end_class

end_unit

