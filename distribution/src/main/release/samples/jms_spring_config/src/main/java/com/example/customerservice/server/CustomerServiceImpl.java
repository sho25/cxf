begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|com
operator|.
name|example
operator|.
name|customerservice
operator|.
name|server
package|;
end_package

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigDecimal
import|;
end_import

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
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|GregorianCalendar
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
name|javax
operator|.
name|annotation
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|example
operator|.
name|customerservice
operator|.
name|Customer
import|;
end_import

begin_import
import|import
name|com
operator|.
name|example
operator|.
name|customerservice
operator|.
name|CustomerService
import|;
end_import

begin_import
import|import
name|com
operator|.
name|example
operator|.
name|customerservice
operator|.
name|CustomerType
import|;
end_import

begin_import
import|import
name|com
operator|.
name|example
operator|.
name|customerservice
operator|.
name|NoSuchCustomer
import|;
end_import

begin_import
import|import
name|com
operator|.
name|example
operator|.
name|customerservice
operator|.
name|NoSuchCustomerException
import|;
end_import

begin_class
specifier|public
class|class
name|CustomerServiceImpl
implements|implements
name|CustomerService
block|{
comment|/**      * The WebServiceContext can be used to retrieve special attributes like the       * user principal. Normally it is not needed      */
annotation|@
name|Resource
name|WebServiceContext
name|wsContext
decl_stmt|;
specifier|public
name|List
argument_list|<
name|Customer
argument_list|>
name|getCustomersByName
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|NoSuchCustomerException
block|{
if|if
condition|(
literal|"None"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|NoSuchCustomer
name|noSuchCustomer
init|=
operator|new
name|NoSuchCustomer
argument_list|()
decl_stmt|;
name|noSuchCustomer
operator|.
name|setCustomerName
argument_list|(
name|name
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|NoSuchCustomerException
argument_list|(
literal|"Did not find any matching customer for name="
operator|+
name|name
argument_list|,
name|noSuchCustomer
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|Customer
argument_list|>
name|customers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|c
init|=
literal|0
init|;
name|c
operator|<
literal|2
condition|;
name|c
operator|++
control|)
block|{
name|Customer
name|cust
init|=
operator|new
name|Customer
argument_list|()
decl_stmt|;
name|cust
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|cust
operator|.
name|getAddress
argument_list|()
operator|.
name|add
argument_list|(
literal|"Pine Street 200"
argument_list|)
expr_stmt|;
name|Date
name|bDate
init|=
operator|new
name|GregorianCalendar
argument_list|(
literal|2009
argument_list|,
literal|01
argument_list|,
literal|01
argument_list|)
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|cust
operator|.
name|setBirthDate
argument_list|(
name|bDate
argument_list|)
expr_stmt|;
name|cust
operator|.
name|setNumOrders
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|cust
operator|.
name|setRevenue
argument_list|(
literal|10000
argument_list|)
expr_stmt|;
name|cust
operator|.
name|setTest
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|1.5
argument_list|)
argument_list|)
expr_stmt|;
name|cust
operator|.
name|setType
argument_list|(
name|CustomerType
operator|.
name|BUSINESS
argument_list|)
expr_stmt|;
name|customers
operator|.
name|add
argument_list|(
name|cust
argument_list|)
expr_stmt|;
block|}
return|return
name|customers
return|;
block|}
specifier|public
name|void
name|updateCustomer
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
literal|"update request was received"
argument_list|)
expr_stmt|;
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|10000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// Nothing to do here
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Customer was updated"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

