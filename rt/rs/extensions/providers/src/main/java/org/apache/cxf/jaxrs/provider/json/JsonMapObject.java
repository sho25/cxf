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
operator|.
name|provider
operator|.
name|json
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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

begin_class
specifier|public
class|class
name|JsonMapObject
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|updateCount
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|values
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|JsonMapObject
parameter_list|()
block|{              }
specifier|public
name|JsonMapObject
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|values
parameter_list|)
block|{
name|this
operator|.
name|values
operator|=
name|values
expr_stmt|;
block|}
specifier|public
name|void
name|setProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|values
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
if|if
condition|(
name|updateCount
operator|==
literal|null
condition|)
block|{
name|updateCount
operator|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|Integer
name|count
init|=
name|updateCount
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|count
operator|=
name|count
operator|==
literal|null
condition|?
literal|2
else|:
name|count
operator|++
expr_stmt|;
name|updateCount
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
name|values
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Object
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|values
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|asMap
parameter_list|()
block|{
return|return
name|values
return|;
block|}
specifier|public
name|Integer
name|getIntegerProperty
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Object
name|value
init|=
name|getProperty
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
return|return
name|value
operator|instanceof
name|Integer
condition|?
operator|(
name|Integer
operator|)
name|value
else|:
name|Integer
operator|.
name|parseInt
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|Long
name|getLongProperty
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Object
name|value
init|=
name|getProperty
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
return|return
name|value
operator|instanceof
name|Long
condition|?
operator|(
name|Long
operator|)
name|value
else|:
name|Long
operator|.
name|parseLong
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|values
operator|.
name|hashCode
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
return|return
name|obj
operator|instanceof
name|JsonMapObject
operator|&&
operator|(
operator|(
name|JsonMapObject
operator|)
name|obj
operator|)
operator|.
name|values
operator|.
name|equals
argument_list|(
name|this
operator|.
name|values
argument_list|)
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getUpdateCount
parameter_list|()
block|{
return|return
name|updateCount
operator|==
literal|null
condition|?
literal|null
else|:
name|Collections
operator|.
expr|<
name|String
operator|,
name|Object
operator|>
name|unmodifiableMap
argument_list|(
name|updateCount
argument_list|)
return|;
block|}
block|}
end_class

end_unit

