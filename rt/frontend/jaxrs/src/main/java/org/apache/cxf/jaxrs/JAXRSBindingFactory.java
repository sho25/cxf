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
name|jaxrs
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
name|binding
operator|.
name|AbstractBindingFactory
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
name|Binding
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
name|xml
operator|.
name|XMLBinding
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
name|xml
operator|.
name|interceptor
operator|.
name|XMLFaultOutInterceptor
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
name|interceptor
operator|.
name|StaxOutInterceptor
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
name|jaxrs
operator|.
name|interceptor
operator|.
name|JAXRSInInterceptor
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
name|jaxrs
operator|.
name|interceptor
operator|.
name|JAXRSOutInterceptor
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
name|Service
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
name|model
operator|.
name|BindingInfo
import|;
end_import

begin_class
annotation|@
name|NoJSR250Annotations
argument_list|(
name|unlessNull
operator|=
block|{
literal|"bus"
block|}
argument_list|)
specifier|public
class|class
name|JAXRSBindingFactory
extends|extends
name|AbstractBindingFactory
block|{
specifier|public
specifier|static
specifier|final
name|String
name|JAXRS_BINDING_ID
init|=
literal|"http://apache.org/cxf/binding/jaxrs"
decl_stmt|;
specifier|public
name|JAXRSBindingFactory
parameter_list|()
block|{     }
specifier|public
name|Binding
name|createBinding
parameter_list|(
name|BindingInfo
name|bi
parameter_list|)
block|{
name|XMLBinding
name|binding
init|=
operator|new
name|XMLBinding
argument_list|(
name|bi
argument_list|)
decl_stmt|;
name|binding
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|JAXRSInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|JAXRSOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|XMLFaultOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|StaxOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|binding
return|;
block|}
comment|/*      * The concept of Binding can not be applied to JAX-RS. Here we use      * Binding merely to make this JAX-RS impl compatible with CXF framework      */
specifier|public
name|BindingInfo
name|createBindingInfo
parameter_list|(
name|Service
name|service
parameter_list|,
name|String
name|namespace
parameter_list|,
name|Object
name|obj
parameter_list|)
block|{
name|BindingInfo
name|info
init|=
operator|new
name|BindingInfo
argument_list|(
literal|null
argument_list|,
name|JAXRSBindingFactory
operator|.
name|JAXRS_BINDING_ID
argument_list|)
decl_stmt|;
return|return
name|info
return|;
block|}
block|}
end_class

end_unit

