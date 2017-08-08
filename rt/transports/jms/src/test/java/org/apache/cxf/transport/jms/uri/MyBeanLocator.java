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
name|transport
operator|.
name|jms
operator|.
name|uri
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Map
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
name|configuration
operator|.
name|ConfiguredBeanLocator
import|;
end_import

begin_class
specifier|public
class|class
name|MyBeanLocator
implements|implements
name|ConfiguredBeanLocator
block|{
name|ConfiguredBeanLocator
name|cbl
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|registry
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|MyBeanLocator
parameter_list|(
name|ConfiguredBeanLocator
name|cbl
parameter_list|)
block|{
name|this
operator|.
name|cbl
operator|=
name|cbl
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getBeanOfType
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
name|registry
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|registry
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
return|return
name|cbl
operator|.
name|getBeanOfType
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Collection
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|getBeansOfType
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|List
argument_list|<
name|T
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|registry
operator|.
name|keySet
argument_list|()
control|)
block|{
name|Object
name|bean
init|=
name|registry
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|bean
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
operator|(
name|T
operator|)
name|bean
argument_list|)
expr_stmt|;
block|}
block|}
name|result
operator|.
name|addAll
argument_list|(
name|cbl
operator|.
name|getBeansOfType
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|boolean
name|loadBeansOfType
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|BeanLoaderListener
argument_list|<
name|T
argument_list|>
name|listener
parameter_list|)
block|{
return|return
name|cbl
operator|.
name|loadBeansOfType
argument_list|(
name|type
argument_list|,
name|listener
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|hasConfiguredPropertyValue
parameter_list|(
name|String
name|beanName
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|String
name|value
parameter_list|)
block|{
return|return
name|cbl
operator|.
name|hasConfiguredPropertyValue
argument_list|(
name|beanName
argument_list|,
name|propertyName
argument_list|,
name|value
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getBeanNamesOfType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|cbl
operator|.
name|getBeanNamesOfType
argument_list|(
name|type
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|hasBeanOfName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|cbl
operator|.
name|hasBeanOfName
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|void
name|register
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|object
parameter_list|)
block|{
name|registry
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

