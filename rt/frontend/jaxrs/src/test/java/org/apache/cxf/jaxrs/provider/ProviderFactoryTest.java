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
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

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
name|InputStream
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
name|List
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
name|ConsumeMime
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
name|ProduceMime
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
name|abdera
operator|.
name|model
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|model
operator|.
name|Feed
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
name|helpers
operator|.
name|IOUtils
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
name|JAXRSUtils
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
name|ProviderFactoryTest
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
name|ProviderFactory
operator|.
name|getInstance
argument_list|()
operator|.
name|clearUserMessageProviders
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSortEntityProviders
parameter_list|()
throws|throws
name|Exception
block|{
name|ProviderFactory
name|pf
init|=
name|ProviderFactory
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|pf
operator|.
name|registerUserEntityProvider
argument_list|(
operator|new
name|TestStringProvider
argument_list|()
argument_list|)
expr_stmt|;
name|pf
operator|.
name|registerUserEntityProvider
argument_list|(
operator|new
name|StringProvider
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|MessageBodyReader
argument_list|>
name|readers
init|=
name|pf
operator|.
name|getUserMessageReaders
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|indexOf
argument_list|(
name|readers
argument_list|,
name|TestStringProvider
operator|.
name|class
argument_list|)
operator|<
name|indexOf
argument_list|(
name|readers
argument_list|,
name|StringProvider
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|MessageBodyWriter
argument_list|>
name|writers
init|=
name|pf
operator|.
name|getUserMessageWriters
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|indexOf
argument_list|(
name|writers
argument_list|,
name|TestStringProvider
operator|.
name|class
argument_list|)
operator|<
name|indexOf
argument_list|(
name|writers
argument_list|,
name|StringProvider
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
comment|//REVISIT the compare algorithm
comment|//assertTrue(indexOf(providers, JSONProvider.class)< indexOf(providers, TestStringProvider.class));
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetStringProvider
parameter_list|()
throws|throws
name|Exception
block|{
name|verifyProvider
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|StringProvider
operator|.
name|class
argument_list|,
literal|"text/html"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBinaryProvider
parameter_list|()
throws|throws
name|Exception
block|{
name|verifyProvider
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|BinaryDataProvider
operator|.
name|class
argument_list|,
literal|"*/*"
argument_list|)
expr_stmt|;
name|verifyProvider
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|BinaryDataProvider
operator|.
name|class
argument_list|,
literal|"image/png"
argument_list|)
expr_stmt|;
name|MessageBodyWriter
name|writer
init|=
name|ProviderFactory
operator|.
name|getInstance
argument_list|()
operator|.
name|createMessageBodyWriter
argument_list|(
name|File
operator|.
name|class
argument_list|,
name|JAXRSUtils
operator|.
name|ALL_TYPES
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|BinaryDataProvider
operator|.
name|class
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
parameter_list|,
name|String
name|errorMessage
parameter_list|)
throws|throws
name|Exception
block|{
name|MediaType
name|mType
init|=
name|MediaType
operator|.
name|parse
argument_list|(
name|mediaType
argument_list|)
decl_stmt|;
name|MessageBodyReader
name|reader
init|=
name|ProviderFactory
operator|.
name|getInstance
argument_list|()
operator|.
name|createMessageBodyReader
argument_list|(
name|type
argument_list|,
name|mType
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|errorMessage
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
name|writer
init|=
name|ProviderFactory
operator|.
name|getInstance
argument_list|()
operator|.
name|createMessageBodyWriter
argument_list|(
name|type
argument_list|,
name|mType
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|errorMessage
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
name|type
argument_list|,
name|provider
argument_list|,
name|mediaType
argument_list|,
literal|"Unexpected provider found"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetStringProviderWildCard
parameter_list|()
throws|throws
name|Exception
block|{
name|verifyProvider
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|StringProvider
operator|.
name|class
argument_list|,
literal|"text/*"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetAtomProvider
parameter_list|()
throws|throws
name|Exception
block|{
name|verifyProvider
argument_list|(
name|Entry
operator|.
name|class
argument_list|,
name|AtomEntryProvider
operator|.
name|class
argument_list|,
literal|"application/atom+xml"
argument_list|)
expr_stmt|;
name|verifyProvider
argument_list|(
name|Feed
operator|.
name|class
argument_list|,
name|AtomFeedProvider
operator|.
name|class
argument_list|,
literal|"application/atom+xml"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetStringProviderUsingProviderDeclaration
parameter_list|()
throws|throws
name|Exception
block|{
name|ProviderFactory
name|pf
init|=
name|ProviderFactory
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|pf
operator|.
name|registerUserEntityProvider
argument_list|(
operator|new
name|TestStringProvider
argument_list|()
argument_list|)
expr_stmt|;
name|verifyProvider
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|TestStringProvider
operator|.
name|class
argument_list|,
literal|"text/html"
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
annotation|@
name|Test
specifier|public
name|void
name|testRegisterCustomJSONEntityProvider
parameter_list|()
throws|throws
name|Exception
block|{
name|ProviderFactory
name|pf
init|=
name|ProviderFactory
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|pf
operator|.
name|registerUserEntityProvider
argument_list|(
operator|new
name|CustomJSONProvider
argument_list|()
argument_list|)
expr_stmt|;
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
name|CustomJSONProvider
operator|.
name|class
argument_list|,
literal|"application/json"
argument_list|,
literal|"User-registered provider was not returned first"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRegisterCustomEntityProvider
parameter_list|()
throws|throws
name|Exception
block|{
name|ProviderFactory
name|pf
init|=
operator|(
name|ProviderFactory
operator|)
name|ProviderFactory
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|pf
operator|.
name|registerUserEntityProvider
argument_list|(
operator|new
name|CustomWidgetProvider
argument_list|()
argument_list|)
expr_stmt|;
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
name|CustomWidgetProvider
operator|.
name|class
argument_list|,
literal|"application/widget"
argument_list|,
literal|"User-registered provider was not returned first"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|int
name|indexOf
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|Object
argument_list|>
name|providers
parameter_list|,
name|Class
name|providerType
parameter_list|)
block|{
name|int
name|index
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Object
name|p
range|:
name|providers
control|)
block|{
if|if
condition|(
name|p
operator|.
name|getClass
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|providerType
argument_list|)
condition|)
block|{
break|break;
block|}
name|index
operator|++
expr_stmt|;
block|}
return|return
name|index
return|;
block|}
annotation|@
name|ConsumeMime
argument_list|(
literal|"text/html"
argument_list|)
annotation|@
name|ProduceMime
argument_list|(
literal|"text/html"
argument_list|)
specifier|private
specifier|final
class|class
name|TestStringProvider
implements|implements
name|MessageBodyReader
argument_list|<
name|String
argument_list|>
implements|,
name|MessageBodyWriter
argument_list|<
name|String
argument_list|>
block|{
specifier|public
name|boolean
name|isReadable
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|type
operator|==
name|String
operator|.
name|class
return|;
block|}
specifier|public
name|boolean
name|isWriteable
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|type
operator|==
name|String
operator|.
name|class
return|;
block|}
specifier|public
name|long
name|getSize
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|s
operator|.
name|length
argument_list|()
return|;
block|}
specifier|public
name|String
name|readFrom
parameter_list|(
name|Class
argument_list|<
name|String
argument_list|>
name|type
parameter_list|,
name|MediaType
name|m
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|InputStream
name|is
parameter_list|)
block|{
try|try
block|{
return|return
name|IOUtils
operator|.
name|toString
argument_list|(
name|is
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// TODO: better exception handling
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|writeTo
parameter_list|(
name|String
name|obj
parameter_list|,
name|MediaType
name|m
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|OutputStream
name|os
parameter_list|)
block|{
try|try
block|{
name|os
operator|.
name|write
argument_list|(
name|obj
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
comment|// TODO: better exception handling
block|}
block|}
block|}
annotation|@
name|ConsumeMime
argument_list|(
literal|"application/json"
argument_list|)
annotation|@
name|ProduceMime
argument_list|(
literal|"application/json"
argument_list|)
specifier|private
specifier|final
class|class
name|CustomJSONProvider
implements|implements
name|MessageBodyReader
argument_list|<
name|String
argument_list|>
implements|,
name|MessageBodyWriter
argument_list|<
name|String
argument_list|>
block|{
specifier|public
name|boolean
name|isReadable
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|type
operator|.
name|getAnnotation
argument_list|(
name|XmlRootElement
operator|.
name|class
argument_list|)
operator|!=
literal|null
return|;
block|}
specifier|public
name|boolean
name|isWriteable
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|type
operator|.
name|getAnnotation
argument_list|(
name|XmlRootElement
operator|.
name|class
argument_list|)
operator|!=
literal|null
return|;
block|}
specifier|public
name|long
name|getSize
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|s
operator|.
name|length
argument_list|()
return|;
block|}
specifier|public
name|String
name|readFrom
parameter_list|(
name|Class
argument_list|<
name|String
argument_list|>
name|type
parameter_list|,
name|MediaType
name|m
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|InputStream
name|is
parameter_list|)
block|{
comment|//Dummy
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|writeTo
parameter_list|(
name|String
name|obj
parameter_list|,
name|MediaType
name|m
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|OutputStream
name|os
parameter_list|)
block|{
comment|//Dummy
block|}
block|}
annotation|@
name|ConsumeMime
argument_list|(
literal|"application/widget"
argument_list|)
annotation|@
name|ProduceMime
argument_list|(
literal|"application/widget"
argument_list|)
specifier|private
specifier|final
class|class
name|CustomWidgetProvider
implements|implements
name|MessageBodyReader
argument_list|<
name|String
argument_list|>
implements|,
name|MessageBodyWriter
argument_list|<
name|String
argument_list|>
block|{
specifier|public
name|boolean
name|isReadable
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|type
operator|.
name|getAnnotation
argument_list|(
name|XmlRootElement
operator|.
name|class
argument_list|)
operator|!=
literal|null
return|;
block|}
specifier|public
name|boolean
name|isWriteable
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|type
operator|.
name|getAnnotation
argument_list|(
name|XmlRootElement
operator|.
name|class
argument_list|)
operator|!=
literal|null
return|;
block|}
specifier|public
name|long
name|getSize
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|s
operator|.
name|length
argument_list|()
return|;
block|}
specifier|public
name|String
name|readFrom
parameter_list|(
name|Class
argument_list|<
name|String
argument_list|>
name|type
parameter_list|,
name|MediaType
name|m
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|InputStream
name|is
parameter_list|)
block|{
comment|//Dummy
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|writeTo
parameter_list|(
name|String
name|obj
parameter_list|,
name|MediaType
name|m
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|OutputStream
name|os
parameter_list|)
block|{
comment|//Dummy
block|}
block|}
block|}
end_class

end_unit

