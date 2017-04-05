begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|jaxrs
operator|.
name|service
package|;
end_package

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
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|WebApplicationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
import|;
end_import

begin_class
specifier|public
class|class
name|CustomerServiceImpl
implements|implements
name|CustomerService
block|{
name|long
name|currentId
init|=
literal|123
decl_stmt|;
name|Map
argument_list|<
name|Long
argument_list|,
name|Customer
argument_list|>
name|customers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Long
argument_list|,
name|Order
argument_list|>
name|orders
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|CustomerServiceImpl
parameter_list|()
block|{
name|init
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Customer
name|getCustomer
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"----invoking getCustomer, Customer id is: "
operator|+
name|id
argument_list|)
expr_stmt|;
name|long
name|idNumber
init|=
name|Long
operator|.
name|parseLong
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|Customer
name|c
init|=
name|customers
operator|.
name|get
argument_list|(
name|idNumber
argument_list|)
decl_stmt|;
return|return
name|c
return|;
block|}
specifier|public
name|Response
name|updateCustomer
parameter_list|(
name|Long
name|id
parameter_list|,
name|Customer
name|customer
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"----invoking updateCustomer, Customer name is: "
operator|+
name|customer
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Customer
name|c
init|=
name|customers
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|null
operator|||
name|c
operator|.
name|getId
argument_list|()
operator|!=
name|customer
operator|.
name|getId
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|()
throw|;
block|}
name|customers
operator|.
name|put
argument_list|(
name|customer
operator|.
name|getId
argument_list|()
argument_list|,
name|customer
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|customer
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
specifier|public
name|Response
name|addCustomer
parameter_list|(
name|Customer
name|customer
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"----invoking addCustomer, Customer name is: "
operator|+
name|customer
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|customer
operator|.
name|setId
argument_list|(
operator|++
name|currentId
argument_list|)
expr_stmt|;
name|customers
operator|.
name|put
argument_list|(
name|customer
operator|.
name|getId
argument_list|()
argument_list|,
name|customer
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|customer
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
specifier|public
name|Response
name|deleteCustomer
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"----invoking deleteCustomer, Customer id is: "
operator|+
name|id
argument_list|)
expr_stmt|;
name|long
name|idNumber
init|=
name|Long
operator|.
name|parseLong
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|Customer
name|c
init|=
name|customers
operator|.
name|remove
argument_list|(
name|idNumber
argument_list|)
decl_stmt|;
name|Response
name|r
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|r
operator|=
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|r
operator|=
name|Response
operator|.
name|notModified
argument_list|()
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
specifier|public
name|Order
name|getOrder
parameter_list|(
name|String
name|orderId
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"----invoking getOrder, Order id is: "
operator|+
name|orderId
argument_list|)
expr_stmt|;
name|long
name|idNumber
init|=
name|Long
operator|.
name|parseLong
argument_list|(
name|orderId
argument_list|)
decl_stmt|;
name|Order
name|c
init|=
name|orders
operator|.
name|get
argument_list|(
name|idNumber
argument_list|)
decl_stmt|;
return|return
name|c
return|;
block|}
specifier|final
name|void
name|init
parameter_list|()
block|{
name|Customer
name|c
init|=
operator|new
name|Customer
argument_list|()
decl_stmt|;
name|c
operator|.
name|setName
argument_list|(
literal|"John"
argument_list|)
expr_stmt|;
name|c
operator|.
name|setId
argument_list|(
literal|123
argument_list|)
expr_stmt|;
name|customers
operator|.
name|put
argument_list|(
name|c
operator|.
name|getId
argument_list|()
argument_list|,
name|c
argument_list|)
expr_stmt|;
name|Order
name|o
init|=
operator|new
name|Order
argument_list|()
decl_stmt|;
name|o
operator|.
name|setDescription
argument_list|(
literal|"order 223"
argument_list|)
expr_stmt|;
name|o
operator|.
name|setId
argument_list|(
literal|223
argument_list|)
expr_stmt|;
name|orders
operator|.
name|put
argument_list|(
name|o
operator|.
name|getId
argument_list|()
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

