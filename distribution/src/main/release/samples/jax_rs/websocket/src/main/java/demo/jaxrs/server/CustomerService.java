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
name|server
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|Iterator
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
name|concurrent
operator|.
name|ExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|HeaderParam
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
name|PUT
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
name|StreamingOutput
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
name|transport
operator|.
name|websocket
operator|.
name|WebSocketConstants
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"/customerservice/"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
class|class
name|CustomerService
block|{
specifier|private
specifier|static
specifier|final
name|int
name|MAX_ERROR_COUNT
init|=
literal|5
decl_stmt|;
specifier|private
specifier|static
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newSingleThreadExecutor
argument_list|()
decl_stmt|;
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
argument_list|<
name|Long
argument_list|,
name|Customer
argument_list|>
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
argument_list|<
name|Long
argument_list|,
name|Order
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|WriterHolder
argument_list|<
name|OutputStream
argument_list|>
argument_list|>
name|monitors
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|WriterHolder
argument_list|<
name|OutputStream
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|WriterHolder
argument_list|<
name|HttpServletResponse
argument_list|>
argument_list|>
name|monitors2
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|WriterHolder
argument_list|<
name|HttpServletResponse
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|CustomerService
parameter_list|()
block|{
name|init
argument_list|()
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/customers/{id}/"
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
name|customer
init|=
name|customers
operator|.
name|get
argument_list|(
name|idNumber
argument_list|)
decl_stmt|;
if|if
condition|(
name|customer
operator|!=
literal|null
condition|)
block|{
name|sendCustomerEvent
argument_list|(
literal|"retrieved"
argument_list|,
name|customer
argument_list|)
expr_stmt|;
block|}
return|return
name|customer
return|;
block|}
annotation|@
name|PUT
annotation|@
name|Path
argument_list|(
literal|"/customers/"
argument_list|)
specifier|public
name|Response
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
name|customer
operator|.
name|getId
argument_list|()
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
name|sendCustomerEvent
argument_list|(
literal|"updated"
argument_list|,
name|customer
argument_list|)
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
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/customers/"
argument_list|)
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
name|sendCustomerEvent
argument_list|(
literal|"added"
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
annotation|@
name|DELETE
annotation|@
name|Path
argument_list|(
literal|"/customers/{id}/"
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
name|get
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
name|Customer
name|customer
init|=
name|customers
operator|.
name|remove
argument_list|(
name|idNumber
argument_list|)
decl_stmt|;
if|if
condition|(
name|customer
operator|!=
literal|null
condition|)
block|{
name|sendCustomerEvent
argument_list|(
literal|"deleted"
argument_list|,
name|customer
argument_list|)
expr_stmt|;
block|}
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
annotation|@
name|Path
argument_list|(
literal|"/orders/{orderId}/"
argument_list|)
specifier|public
name|Order
name|getOrder
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"orderId"
argument_list|)
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
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/monitor"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/*"
argument_list|)
specifier|public
name|StreamingOutput
name|monitorCustomers
parameter_list|(
annotation|@
name|HeaderParam
argument_list|(
name|WebSocketConstants
operator|.
name|DEFAULT_REQUEST_ID_KEY
argument_list|)
name|String
name|reqid
parameter_list|)
block|{
specifier|final
name|String
name|key
init|=
name|reqid
operator|==
literal|null
condition|?
literal|"*"
else|:
name|reqid
decl_stmt|;
return|return
operator|new
name|StreamingOutput
argument_list|()
block|{
specifier|public
name|void
name|write
parameter_list|(
specifier|final
name|OutputStream
name|out
parameter_list|)
throws|throws
name|IOException
throws|,
name|WebApplicationException
block|{
name|monitors
operator|.
name|put
argument_list|(
name|key
argument_list|,
operator|new
name|WriterHolder
argument_list|(
name|out
argument_list|,
name|MAX_ERROR_COUNT
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
operator|(
literal|"Subscribed at "
operator|+
operator|new
name|java
operator|.
name|util
operator|.
name|Date
argument_list|()
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/monitor2"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/*"
argument_list|)
specifier|public
name|void
name|monitorCustomers2
parameter_list|(
annotation|@
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Context
specifier|final
name|HttpServletResponse
name|httpResponse
parameter_list|,
annotation|@
name|HeaderParam
argument_list|(
name|WebSocketConstants
operator|.
name|DEFAULT_REQUEST_ID_KEY
argument_list|)
name|String
name|reqid
parameter_list|)
block|{
specifier|final
name|String
name|key
init|=
name|reqid
operator|==
literal|null
condition|?
literal|"*"
else|:
name|reqid
decl_stmt|;
name|monitors2
operator|.
name|put
argument_list|(
name|key
argument_list|,
operator|new
name|WriterHolder
argument_list|(
name|httpResponse
argument_list|,
name|MAX_ERROR_COUNT
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|httpResponse
operator|.
name|getOutputStream
argument_list|()
operator|.
name|write
argument_list|(
operator|(
literal|"Subscribed at "
operator|+
operator|new
name|java
operator|.
name|util
operator|.
name|Date
argument_list|()
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/unmonitor/{key}"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/*"
argument_list|)
specifier|public
name|String
name|unmonitorCustomers
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"key"
argument_list|)
name|String
name|key
parameter_list|)
block|{
return|return
operator|(
name|monitors
operator|.
name|remove
argument_list|(
name|key
argument_list|)
operator|!=
literal|null
condition|?
literal|"Removed: "
else|:
literal|"Already removed: "
operator|)
operator|+
name|key
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/unmonitor2/{key}"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/*"
argument_list|)
specifier|public
name|String
name|unmonitorCustomers2
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"key"
argument_list|)
name|String
name|key
parameter_list|)
block|{
return|return
operator|(
name|monitors2
operator|.
name|remove
argument_list|(
name|key
argument_list|)
operator|!=
literal|null
condition|?
literal|"Removed: "
else|:
literal|"Already removed: "
operator|)
operator|+
name|key
return|;
block|}
comment|// CHECKSTYLE:OFF
specifier|private
name|void
name|sendCustomerEvent
parameter_list|(
specifier|final
name|String
name|msg
parameter_list|,
specifier|final
name|Customer
name|customer
parameter_list|)
block|{
name|executor
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|String
name|t
init|=
name|msg
operator|+
literal|": "
operator|+
name|customer
operator|.
name|getId
argument_list|()
operator|+
literal|"/"
operator|+
name|customer
operator|.
name|getName
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|WriterHolder
argument_list|<
name|OutputStream
argument_list|>
argument_list|>
name|it
init|=
name|monitors
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|WriterHolder
argument_list|<
name|OutputStream
argument_list|>
name|wh
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|wh
operator|.
name|getValue
argument_list|()
operator|.
name|write
argument_list|(
name|t
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|wh
operator|.
name|getValue
argument_list|()
operator|.
name|flush
argument_list|()
expr_stmt|;
name|wh
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"----error writing to "
operator|+
name|wh
operator|.
name|getValue
argument_list|()
operator|+
literal|" "
operator|+
name|wh
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|wh
operator|.
name|increment
argument_list|()
condition|)
block|{
comment|// the max error count reached; purging the output resource
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
try|try
block|{
name|wh
operator|.
name|getValue
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e2
parameter_list|)
block|{
comment|// ignore;
block|}
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"----purged "
operator|+
name|wh
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
name|Iterator
argument_list|<
name|WriterHolder
argument_list|<
name|HttpServletResponse
argument_list|>
argument_list|>
name|it
init|=
name|monitors2
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|WriterHolder
argument_list|<
name|HttpServletResponse
argument_list|>
name|wh
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|wh
operator|.
name|getValue
argument_list|()
operator|.
name|getOutputStream
argument_list|()
operator|.
name|write
argument_list|(
name|t
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|wh
operator|.
name|getValue
argument_list|()
operator|.
name|getOutputStream
argument_list|()
operator|.
name|flush
argument_list|()
expr_stmt|;
name|wh
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"----error writing to "
operator|+
name|wh
operator|.
name|getValue
argument_list|()
operator|+
literal|" "
operator|+
name|wh
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|wh
operator|.
name|increment
argument_list|()
condition|)
block|{
comment|// the max error count reached; purging the output resource
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
try|try
block|{
name|wh
operator|.
name|getValue
argument_list|()
operator|.
name|getOutputStream
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e2
parameter_list|)
block|{
comment|// ignore;
block|}
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"----purged "
operator|+
name|wh
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|// CHECKSTYLE:ON
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
name|c
operator|=
operator|new
name|Customer
argument_list|()
expr_stmt|;
name|c
operator|.
name|setName
argument_list|(
literal|"Homer"
argument_list|)
expr_stmt|;
name|c
operator|.
name|setId
argument_list|(
literal|235
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
specifier|private
specifier|static
class|class
name|WriterHolder
parameter_list|<
name|T
parameter_list|>
block|{
specifier|private
specifier|final
name|T
name|value
decl_stmt|;
specifier|private
specifier|final
name|int
name|max
decl_stmt|;
specifier|private
specifier|final
name|AtomicInteger
name|errorCount
decl_stmt|;
name|WriterHolder
parameter_list|(
name|T
name|object
parameter_list|,
name|int
name|max
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|object
expr_stmt|;
name|this
operator|.
name|max
operator|=
name|max
expr_stmt|;
name|this
operator|.
name|errorCount
operator|=
operator|new
name|AtomicInteger
argument_list|()
expr_stmt|;
block|}
specifier|public
name|T
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
specifier|public
name|int
name|get
parameter_list|()
block|{
return|return
name|errorCount
operator|.
name|get
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|increment
parameter_list|()
block|{
return|return
name|max
operator|<
name|errorCount
operator|.
name|getAndIncrement
argument_list|()
return|;
block|}
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|errorCount
operator|.
name|getAndSet
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

