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
name|systest
operator|.
name|jaxrs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|BytesMessage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|ConnectionFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Destination
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|JMSException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageProducer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|InitialContext
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
name|QueryParam
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
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
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
name|Marshaller
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
name|ext
operator|.
name|Oneway
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
name|ext
operator|.
name|ProtocolHeaders
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
name|testutil
operator|.
name|common
operator|.
name|EmbeddedJMSBrokerLauncher
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"/bookstore"
argument_list|)
specifier|public
class|class
name|JMSBookStore
block|{
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
specifier|private
name|ProtocolHeaders
name|headers
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Long
argument_list|,
name|Book
argument_list|>
name|books
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|JMSBookStore
parameter_list|()
block|{
name|books
operator|.
name|put
argument_list|(
literal|123L
argument_list|,
operator|new
name|Book
argument_list|(
literal|"CXF JMS Rocks"
argument_list|,
literal|123L
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/bookidarray"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/xml"
argument_list|)
specifier|public
name|Book
name|getBookByURLQuery
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"id"
argument_list|)
name|String
index|[]
name|ids
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|ids
operator|==
literal|null
operator|||
name|ids
operator|.
name|length
operator|!=
literal|3
condition|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|()
throw|;
block|}
return|return
name|doGetBook
argument_list|(
name|ids
index|[
literal|0
index|]
operator|+
name|ids
index|[
literal|1
index|]
operator|+
name|ids
index|[
literal|2
index|]
argument_list|)
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/books/{bookId}/"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/xml"
argument_list|)
specifier|public
name|Book
name|getBook
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"bookId"
argument_list|)
name|String
name|id
parameter_list|)
throws|throws
name|BookNotFoundFault
block|{
return|return
name|doGetBook
argument_list|(
name|id
argument_list|)
return|;
block|}
annotation|@
name|Path
argument_list|(
literal|"/booksubresource/{bookId}/"
argument_list|)
specifier|public
name|Book
name|getBookSubResource
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"bookId"
argument_list|)
name|String
name|id
parameter_list|)
throws|throws
name|BookNotFoundFault
block|{
return|return
name|doGetBook
argument_list|(
name|id
argument_list|)
return|;
block|}
specifier|private
name|Book
name|doGetBook
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|BookNotFoundFault
block|{
name|Book
name|book
init|=
name|books
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
name|book
operator|!=
literal|null
condition|)
block|{
return|return
name|book
return|;
block|}
name|BookNotFoundDetails
name|details
init|=
operator|new
name|BookNotFoundDetails
argument_list|()
decl_stmt|;
name|details
operator|.
name|setId
argument_list|(
name|Long
operator|.
name|parseLong
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|BookNotFoundFault
argument_list|(
name|details
argument_list|)
throw|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/books"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"application/xml"
argument_list|)
specifier|public
name|Response
name|addBook
parameter_list|(
name|Book
name|book
parameter_list|)
block|{
name|String
name|ct1
init|=
name|headers
operator|.
name|getRequestHeaderValue
argument_list|(
literal|"Content-Type"
argument_list|)
decl_stmt|;
name|String
name|ct2
init|=
name|headers
operator|.
name|getRequestHeader
argument_list|(
literal|"Content-Type"
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|String
name|ct3
init|=
name|headers
operator|.
name|getRequestHeaders
argument_list|()
operator|.
name|getFirst
argument_list|(
literal|"Content-Type"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
literal|"application/xml"
operator|.
name|equals
argument_list|(
name|ct1
argument_list|)
operator|&&
name|ct1
operator|.
name|equals
argument_list|(
name|ct2
argument_list|)
operator|&&
name|ct1
operator|.
name|equals
argument_list|(
name|ct3
argument_list|)
operator|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unexpected content type"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
literal|"custom.value"
operator|.
name|equals
argument_list|(
name|headers
operator|.
name|getRequestHeaderValue
argument_list|(
literal|"custom.protocol.header"
argument_list|)
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Custom header is not set"
argument_list|)
throw|;
block|}
name|book
operator|.
name|setId
argument_list|(
literal|124
argument_list|)
expr_stmt|;
name|books
operator|.
name|put
argument_list|(
name|book
operator|.
name|getId
argument_list|()
argument_list|,
name|book
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|book
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|PUT
annotation|@
name|Path
argument_list|(
literal|"/oneway"
argument_list|)
annotation|@
name|Consumes
argument_list|()
annotation|@
name|Oneway
specifier|public
name|void
name|onewayRequest
parameter_list|(
name|Book
name|book
parameter_list|)
throws|throws
name|Exception
block|{
name|Context
name|ctx
init|=
name|getContext
argument_list|()
decl_stmt|;
name|ConnectionFactory
name|factory
init|=
operator|(
name|ConnectionFactory
operator|)
name|ctx
operator|.
name|lookup
argument_list|(
literal|"ConnectionFactory"
argument_list|)
decl_stmt|;
name|Destination
name|replyToDestination
init|=
operator|(
name|Destination
operator|)
name|ctx
operator|.
name|lookup
argument_list|(
literal|"dynamicQueues/test.jmstransport.response"
argument_list|)
decl_stmt|;
name|Connection
name|connection
init|=
literal|null
decl_stmt|;
try|try
block|{
name|connection
operator|=
name|factory
operator|.
name|createConnection
argument_list|()
expr_stmt|;
name|connection
operator|.
name|start
argument_list|()
expr_stmt|;
name|Session
name|session
init|=
name|connection
operator|.
name|createSession
argument_list|(
literal|false
argument_list|,
name|Session
operator|.
name|AUTO_ACKNOWLEDGE
argument_list|)
decl_stmt|;
name|postOneWayBook
argument_list|(
name|session
argument_list|,
name|replyToDestination
argument_list|,
name|book
argument_list|)
expr_stmt|;
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
if|if
condition|(
name|connection
operator|!=
literal|null
condition|)
block|{
name|connection
operator|.
name|stop
argument_list|()
expr_stmt|;
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|JMSException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
specifier|private
name|Context
name|getContext
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|setProperty
argument_list|(
name|Context
operator|.
name|INITIAL_CONTEXT_FACTORY
argument_list|,
literal|"org.apache.activemq.jndi.ActiveMQInitialContextFactory"
argument_list|)
expr_stmt|;
name|props
operator|.
name|setProperty
argument_list|(
name|Context
operator|.
name|PROVIDER_URL
argument_list|,
literal|"tcp://localhost:"
operator|+
name|EmbeddedJMSBrokerLauncher
operator|.
name|PORT
argument_list|)
expr_stmt|;
return|return
operator|new
name|InitialContext
argument_list|(
name|props
argument_list|)
return|;
block|}
specifier|private
name|void
name|postOneWayBook
parameter_list|(
name|Session
name|session
parameter_list|,
name|Destination
name|destination
parameter_list|,
name|Book
name|book
parameter_list|)
throws|throws
name|Exception
block|{
name|MessageProducer
name|producer
init|=
name|session
operator|.
name|createProducer
argument_list|(
name|destination
argument_list|)
decl_stmt|;
name|BytesMessage
name|message
init|=
name|session
operator|.
name|createBytesMessage
argument_list|()
decl_stmt|;
name|message
operator|.
name|writeBytes
argument_list|(
name|writeBook
argument_list|(
name|book
argument_list|)
argument_list|)
expr_stmt|;
name|producer
operator|.
name|send
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|producer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
name|byte
index|[]
name|writeBook
parameter_list|(
name|Book
name|b
parameter_list|)
throws|throws
name|Exception
block|{
name|JAXBContext
name|c
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
operator|new
name|Class
index|[]
block|{
name|Book
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|Marshaller
name|m
init|=
name|c
operator|.
name|createMarshaller
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|m
operator|.
name|marshal
argument_list|(
name|b
argument_list|,
name|bos
argument_list|)
expr_stmt|;
return|return
name|bos
operator|.
name|toByteArray
argument_list|()
return|;
block|}
block|}
end_class

end_unit

