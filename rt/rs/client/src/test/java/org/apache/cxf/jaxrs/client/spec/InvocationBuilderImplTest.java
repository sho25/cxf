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
name|client
operator|.
name|spec
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|Client
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
name|client
operator|.
name|ClientBuilder
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
name|client
operator|.
name|ClientRequestContext
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
name|client
operator|.
name|ClientRequestFilter
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
name|client
operator|.
name|Invocation
operator|.
name|Builder
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
name|MultivaluedHashMap
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
name|MultivaluedMap
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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|InvocationBuilderImplTest
block|{
specifier|public
specifier|static
class|class
name|TestFilter
implements|implements
name|ClientRequestFilter
block|{
comment|/** {@inheritDoc}*/
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ClientRequestContext
name|context
parameter_list|)
throws|throws
name|IOException
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|context
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|StringBuilder
name|entity
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|headers
operator|.
name|keySet
argument_list|()
control|)
block|{
name|entity
operator|.
name|append
argument_list|(
name|key
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|headers
operator|.
name|getFirst
argument_list|(
name|key
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|";"
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|abortWith
argument_list|(
name|Response
operator|.
name|ok
argument_list|(
name|entity
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHeadersMethod
parameter_list|()
block|{
comment|// the javadoc for the Invocation.Builder.headers(MultivaluedMap) method says that
comment|// invoking this method should remove all previously existing headers
name|Client
name|client
init|=
name|ClientBuilder
operator|.
name|newClient
argument_list|()
operator|.
name|register
argument_list|(
name|TestFilter
operator|.
name|class
argument_list|)
decl_stmt|;
name|Builder
name|builder
init|=
name|client
operator|.
name|target
argument_list|(
literal|"http://localhost:8080/notReal"
argument_list|)
operator|.
name|request
argument_list|()
decl_stmt|;
name|builder
operator|.
name|header
argument_list|(
literal|"Header1"
argument_list|,
literal|"a"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|header
argument_list|(
literal|"UnexpectedHeader"
argument_list|,
literal|"should be removed"
argument_list|)
expr_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|MultivaluedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|putSingle
argument_list|(
literal|"Header1"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|headers
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|Response
name|response
init|=
name|builder
operator|.
name|get
argument_list|()
decl_stmt|;
name|String
name|sentHeaders
init|=
name|response
operator|.
name|readEntity
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|sentHeaders
operator|.
name|contains
argument_list|(
literal|"Header1=b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|sentHeaders
operator|.
name|contains
argument_list|(
literal|"UnexpectedHeader"
argument_list|)
argument_list|)
expr_stmt|;
comment|// null headers map should clear all headers
name|builder
operator|.
name|headers
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|response
operator|=
name|builder
operator|.
name|get
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|response
operator|.
name|readEntity
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

