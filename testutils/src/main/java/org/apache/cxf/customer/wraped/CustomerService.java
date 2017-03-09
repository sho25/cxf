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
name|customer
operator|.
name|wraped
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
name|jws
operator|.
name|WebMethod
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebResult
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
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
name|customer
operator|.
name|Customer
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
name|customer
operator|.
name|Customers
import|;
end_import

begin_comment
comment|// END SNIPPET: service
end_comment

begin_class
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/jra"
argument_list|)
specifier|public
class|class
name|CustomerService
block|{
name|long
name|currentId
init|=
literal|1
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
specifier|public
name|CustomerService
parameter_list|()
block|{
name|Customer
name|customer
init|=
name|createCustomer
argument_list|()
decl_stmt|;
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
block|}
annotation|@
name|WebMethod
annotation|@
name|WebResult
argument_list|(
name|name
operator|=
literal|"customers"
argument_list|)
specifier|public
name|Customers
name|getCustomers
parameter_list|()
block|{
name|Customers
name|cbean
init|=
operator|new
name|Customers
argument_list|()
decl_stmt|;
name|cbean
operator|.
name|setCustomer
argument_list|(
name|customers
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|cbean
return|;
block|}
annotation|@
name|WebMethod
annotation|@
name|WebResult
argument_list|(
name|name
operator|=
literal|"customer"
argument_list|)
specifier|public
name|Customer
name|getCustomer
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"id"
argument_list|)
name|Long
name|id
parameter_list|)
block|{
return|return
name|customers
operator|.
name|get
argument_list|(
name|id
argument_list|)
return|;
block|}
annotation|@
name|WebMethod
specifier|public
name|void
name|updateCustomer
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"id"
argument_list|)
name|String
name|id
parameter_list|,
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"customer"
argument_list|)
name|Customer
name|c
parameter_list|)
block|{
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
block|}
annotation|@
name|WebMethod
specifier|public
name|void
name|addCustomer
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"customer"
argument_list|)
name|Customer
name|c
parameter_list|)
block|{
name|long
name|id
init|=
operator|++
name|currentId
decl_stmt|;
name|c
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|customers
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
annotation|@
name|WebMethod
specifier|public
name|void
name|deleteCustomer
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|customers
operator|.
name|remove
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Customer
name|createCustomer
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
literal|"Dan Diephouse"
argument_list|)
expr_stmt|;
name|c
operator|.
name|setId
argument_list|(
literal|123
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
block|}
end_class

begin_comment
comment|// END SNIPPET: service
end_comment

end_unit

