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
name|jaxws22
operator|.
name|spring
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
name|bus
operator|.
name|spring
operator|.
name|BusWiringBeanFactoryPostProcessor
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
name|springframework
operator|.
name|beans
operator|.
name|BeansException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContextAware
import|;
end_import

begin_class
annotation|@
name|NoJSR250Annotations
specifier|public
class|class
name|JAXWS22SpringEndpointImpl
extends|extends
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws22
operator|.
name|EndpointImpl
implements|implements
name|ApplicationContextAware
block|{
specifier|public
name|JAXWS22SpringEndpointImpl
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|super
argument_list|(
name|o
operator|instanceof
name|Bus
condition|?
operator|(
name|Bus
operator|)
name|o
else|:
literal|null
argument_list|,
name|o
operator|instanceof
name|Bus
condition|?
literal|null
else|:
name|o
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JAXWS22SpringEndpointImpl
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|Object
name|implementor
parameter_list|)
block|{
name|super
argument_list|(
name|bus
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setApplicationContext
parameter_list|(
name|ApplicationContext
name|ctx
parameter_list|)
throws|throws
name|BeansException
block|{
if|if
condition|(
name|getBus
argument_list|()
operator|==
literal|null
condition|)
block|{
name|setBus
argument_list|(
name|BusWiringBeanFactoryPostProcessor
operator|.
name|addDefaultBus
argument_list|(
name|ctx
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

