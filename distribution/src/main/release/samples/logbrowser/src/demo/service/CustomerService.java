begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
operator|.
name|*
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
name|Consumes
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
name|DELETE
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
name|GET
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
name|POST
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
name|Path
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
name|PathParam
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
name|Produces
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

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|Validate
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
name|logging
operator|.
name|LogUtils
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"/customers/"
argument_list|)
specifier|public
class|class
name|CustomerService
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|CustomerService
operator|.
name|class
argument_list|)
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
argument_list|<
name|Long
argument_list|,
name|Customer
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/{id}/"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
specifier|public
name|Customer
name|getCustomer
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
specifier|final
name|String
name|id
parameter_list|)
block|{
name|Validate
operator|.
name|notNull
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|Validate
operator|.
name|notEmpty
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|log
argument_list|(
name|FINE
argument_list|,
literal|"Invoking getCustomer, id={0}"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|Customer
name|customer
init|=
name|customers
operator|.
name|get
argument_list|(
name|Long
operator|.
name|parseLong
argument_list|(
name|id
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|customer
operator|==
literal|null
condition|)
block|{
name|LOGGER
operator|.
name|log
argument_list|(
name|SEVERE
argument_list|,
literal|"Specified customer does not exist, id={0}"
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
return|return
name|customer
return|;
block|}
annotation|@
name|POST
annotation|@
name|Consumes
argument_list|(
literal|"application/json"
argument_list|)
specifier|public
name|Response
name|updateCustomer
parameter_list|(
specifier|final
name|Customer
name|customer
parameter_list|)
block|{
name|Validate
operator|.
name|notNull
argument_list|(
name|customer
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|log
argument_list|(
name|FINE
argument_list|,
literal|"Invoking updateCustomer, customer={0}"
argument_list|,
name|customer
argument_list|)
expr_stmt|;
if|if
condition|(
name|isCustomerExists
argument_list|(
name|customer
argument_list|)
condition|)
block|{
name|LOGGER
operator|.
name|log
argument_list|(
name|FINE
argument_list|,
literal|"Specified customer exists, update data, customer={0}"
argument_list|,
name|customer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOGGER
operator|.
name|log
argument_list|(
name|WARNING
argument_list|,
literal|"Specified customer does not exist, add data, customer={0}"
argument_list|,
name|customer
argument_list|)
expr_stmt|;
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
name|LOGGER
operator|.
name|log
argument_list|(
name|INFO
argument_list|,
literal|"Customer was updated successful, customer={0}"
argument_list|,
name|customer
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|DELETE
annotation|@
name|Path
argument_list|(
literal|"/{id}/"
argument_list|)
specifier|public
name|Response
name|deleteCustomer
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|String
name|id
parameter_list|)
block|{
name|Validate
operator|.
name|notNull
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|Validate
operator|.
name|notEmpty
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|log
argument_list|(
name|FINE
argument_list|,
literal|"Invoking deleteCustomer, id={0}"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|long
name|identifier
init|=
name|Long
operator|.
name|parseLong
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|Response
name|response
decl_stmt|;
if|if
condition|(
name|isCustomerExists
argument_list|(
name|identifier
argument_list|)
condition|)
block|{
name|LOGGER
operator|.
name|log
argument_list|(
name|FINE
argument_list|,
literal|"Specified customer exists, remove data, id={0}"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|customers
operator|.
name|remove
argument_list|(
name|identifier
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|log
argument_list|(
name|INFO
argument_list|,
literal|"Customer was removed successful, id={0}"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|response
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
name|LOGGER
operator|.
name|log
argument_list|(
name|SEVERE
argument_list|,
literal|"Specified customer does not exist, remove fail, id={0}"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|response
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
name|response
return|;
block|}
specifier|private
name|boolean
name|isCustomerExists
parameter_list|(
specifier|final
name|Customer
name|customer
parameter_list|)
block|{
return|return
name|customers
operator|.
name|get
argument_list|(
name|customer
operator|.
name|getId
argument_list|()
argument_list|)
operator|!=
literal|null
return|;
block|}
specifier|private
name|boolean
name|isCustomerExists
parameter_list|(
specifier|final
name|long
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
operator|!=
literal|null
return|;
block|}
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"Customer"
argument_list|)
specifier|public
specifier|static
class|class
name|Customer
block|{
specifier|private
name|long
name|id
decl_stmt|;
specifier|private
name|String
name|name
decl_stmt|;
specifier|public
name|long
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|long
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Customer{id="
operator|+
name|id
operator|+
literal|", name='"
operator|+
name|name
operator|+
literal|"'}"
return|;
block|}
block|}
block|}
end_class

end_unit

