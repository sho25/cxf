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
package|;
end_package

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
name|MediaType
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
name|ext
operator|.
name|MessageBodyReader
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
name|ext
operator|.
name|MessageBodyWriter
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
name|provider
operator|.
name|atom
operator|.
name|AtomPojoProvider
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
name|provider
operator|.
name|json
operator|.
name|JSONProvider
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
name|resources
operator|.
name|Book
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
name|resources
operator|.
name|TagVO
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
name|message
operator|.
name|MessageImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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

begin_class
specifier|public
class|class
name|ProviderFactoryAllTest
extends|extends
name|Assert
block|{
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|ServerProviderFactory
operator|.
name|getInstance
argument_list|()
operator|.
name|clearProviders
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAtomPojoProvider
parameter_list|()
block|{
name|ProviderFactory
name|pf
init|=
name|ServerProviderFactory
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|AtomPojoProvider
name|provider
init|=
operator|new
name|AtomPojoProvider
argument_list|()
decl_stmt|;
name|pf
operator|.
name|registerUserProvider
argument_list|(
name|provider
argument_list|)
expr_stmt|;
name|MessageBodyReader
argument_list|<
name|?
argument_list|>
name|feedReader
init|=
name|pf
operator|.
name|createMessageBodyReader
argument_list|(
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"application/atom+xml"
argument_list|)
argument_list|,
operator|new
name|MessageImpl
argument_list|()
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|feedReader
argument_list|,
name|provider
argument_list|)
expr_stmt|;
name|MessageBodyReader
argument_list|<
name|?
argument_list|>
name|entryReader
init|=
name|pf
operator|.
name|createMessageBodyReader
argument_list|(
name|TagVO
operator|.
name|class
argument_list|,
name|TagVO
operator|.
name|class
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"application/atom+xml;type=entry"
argument_list|)
argument_list|,
operator|new
name|MessageImpl
argument_list|()
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|entryReader
argument_list|,
name|provider
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCustomJsonProvider
parameter_list|()
block|{
name|ProviderFactory
name|pf
init|=
name|ServerProviderFactory
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|JSONProvider
argument_list|<
name|Book
argument_list|>
name|provider
init|=
operator|new
name|JSONProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|pf
operator|.
name|registerUserProvider
argument_list|(
name|provider
argument_list|)
expr_stmt|;
name|MessageBodyReader
argument_list|<
name|?
argument_list|>
name|customJsonReader
init|=
name|pf
operator|.
name|createMessageBodyReader
argument_list|(
name|Book
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|,
operator|new
name|MessageImpl
argument_list|()
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|customJsonReader
argument_list|,
name|provider
argument_list|)
expr_stmt|;
name|MessageBodyWriter
argument_list|<
name|?
argument_list|>
name|customJsonWriter
init|=
name|pf
operator|.
name|createMessageBodyWriter
argument_list|(
name|Book
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|,
operator|new
name|MessageImpl
argument_list|()
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|customJsonWriter
argument_list|,
name|provider
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verifyProvider
parameter_list|(
name|ProviderFactory
name|pf
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|provider
parameter_list|,
name|String
name|mediaType
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|pf
operator|==
literal|null
condition|)
block|{
name|pf
operator|=
name|ServerProviderFactory
operator|.
name|getInstance
argument_list|()
expr_stmt|;
block|}
name|MediaType
name|mType
init|=
name|MediaType
operator|.
name|valueOf
argument_list|(
name|mediaType
argument_list|)
decl_stmt|;
name|MessageBodyReader
argument_list|<
name|?
argument_list|>
name|reader
init|=
name|pf
operator|.
name|createMessageBodyReader
argument_list|(
name|type
argument_list|,
name|type
argument_list|,
literal|null
argument_list|,
name|mType
argument_list|,
operator|new
name|MessageImpl
argument_list|()
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
literal|"Unexpected provider found"
argument_list|,
name|provider
argument_list|,
name|reader
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|MessageBodyWriter
argument_list|<
name|?
argument_list|>
name|writer
init|=
name|pf
operator|.
name|createMessageBodyWriter
argument_list|(
name|type
argument_list|,
name|type
argument_list|,
literal|null
argument_list|,
name|mType
argument_list|,
operator|new
name|MessageImpl
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Unexpected provider found"
argument_list|,
name|provider
operator|==
name|writer
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verifyProvider
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|provider
parameter_list|,
name|String
name|mediaType
parameter_list|)
throws|throws
name|Exception
block|{
name|verifyProvider
argument_list|(
literal|null
argument_list|,
name|type
argument_list|,
name|provider
argument_list|,
name|mediaType
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetJSONProviderConsumeMime
parameter_list|()
throws|throws
name|Exception
block|{
name|verifyProvider
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|resources
operator|.
name|Book
operator|.
name|class
argument_list|,
name|JSONProvider
operator|.
name|class
argument_list|,
literal|"application/json"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

