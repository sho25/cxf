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
name|atom
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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
name|core
operator|.
name|MediaType
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
name|Unmarshaller
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
name|jaxrs
operator|.
name|impl
operator|.
name|MetadataMap
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
name|JAXBElementProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
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
name|assertNotNull
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
name|assertNull
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
name|AtomPojoProviderTest
block|{
specifier|private
name|ClassPathXmlApplicationContext
name|ctx
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|ctx
operator|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"/org/apache/cxf/jaxrs/provider/atom/servers.xml"
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteFeedWithBuilders
parameter_list|()
throws|throws
name|Exception
block|{
name|AtomPojoProvider
name|provider
init|=
operator|(
name|AtomPojoProvider
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"atom"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|provider
argument_list|)
expr_stmt|;
name|provider
operator|.
name|setFormattedOutput
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|Books
name|books
init|=
operator|new
name|Books
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Book
argument_list|>
name|bs
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|bs
operator|.
name|add
argument_list|(
operator|new
name|Book
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|bs
operator|.
name|add
argument_list|(
operator|new
name|Book
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|books
operator|.
name|setBooks
argument_list|(
name|bs
argument_list|)
expr_stmt|;
name|provider
operator|.
name|writeTo
argument_list|(
name|books
argument_list|,
name|Books
operator|.
name|class
argument_list|,
name|Books
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"application/atom+xml"
argument_list|)
argument_list|,
literal|null
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|ByteArrayInputStream
name|bis
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|Feed
name|feed
init|=
operator|new
name|AtomFeedProvider
argument_list|()
operator|.
name|readFrom
argument_list|(
name|Feed
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|bis
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Books"
argument_list|,
name|feed
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Entry
argument_list|>
name|entries
init|=
name|feed
operator|.
name|getEntries
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|entries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|verifyEntry
argument_list|(
name|getEntry
argument_list|(
name|entries
argument_list|,
literal|"a"
argument_list|)
argument_list|,
literal|"a"
argument_list|)
expr_stmt|;
name|verifyEntry
argument_list|(
name|getEntry
argument_list|(
name|entries
argument_list|,
literal|"b"
argument_list|)
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteFeedWithBuildersNoJaxb
parameter_list|()
throws|throws
name|Exception
block|{
name|AtomPojoProvider
name|provider
init|=
operator|(
name|AtomPojoProvider
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"atomNoJaxb"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|provider
argument_list|)
expr_stmt|;
name|provider
operator|.
name|setFormattedOutput
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|Books
name|books
init|=
operator|new
name|Books
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Book
argument_list|>
name|bs
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|bs
operator|.
name|add
argument_list|(
operator|new
name|Book
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|bs
operator|.
name|add
argument_list|(
operator|new
name|Book
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|books
operator|.
name|setBooks
argument_list|(
name|bs
argument_list|)
expr_stmt|;
name|provider
operator|.
name|writeTo
argument_list|(
name|books
argument_list|,
name|Books
operator|.
name|class
argument_list|,
name|Books
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"application/atom+xml"
argument_list|)
argument_list|,
literal|null
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|ByteArrayInputStream
name|bis
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|Feed
name|feed
init|=
operator|new
name|AtomFeedProvider
argument_list|()
operator|.
name|readFrom
argument_list|(
name|Feed
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|bis
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Books"
argument_list|,
name|feed
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Entry
argument_list|>
name|entries
init|=
name|feed
operator|.
name|getEntries
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|entries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Entry
name|entryA
init|=
name|getEntry
argument_list|(
name|entries
argument_list|,
literal|"a"
argument_list|)
decl_stmt|;
name|verifyEntry
argument_list|(
name|entryA
argument_list|,
literal|"a"
argument_list|)
expr_stmt|;
name|String
name|entryAContent
init|=
name|entryA
operator|.
name|getContent
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"<a/>"
operator|.
name|equals
argument_list|(
name|entryAContent
argument_list|)
operator|||
literal|"<a><a/>"
operator|.
name|equals
argument_list|(
name|entryAContent
argument_list|)
operator|||
literal|"<a xmlns=\"\"/>"
operator|.
name|equals
argument_list|(
name|entryAContent
argument_list|)
argument_list|)
expr_stmt|;
name|Entry
name|entryB
init|=
name|getEntry
argument_list|(
name|entries
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|verifyEntry
argument_list|(
name|entryB
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
name|String
name|entryBContent
init|=
name|entryB
operator|.
name|getContent
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"<b/>"
operator|.
name|equals
argument_list|(
name|entryBContent
argument_list|)
operator|||
literal|"<b><b/>"
operator|.
name|equals
argument_list|(
name|entryBContent
argument_list|)
operator|||
literal|"<b xmlns=\"\"/>"
operator|.
name|equals
argument_list|(
name|entryBContent
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteEntryWithBuilders
parameter_list|()
throws|throws
name|Exception
block|{
name|AtomPojoProvider
name|provider
init|=
operator|(
name|AtomPojoProvider
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"atom2"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|provider
argument_list|)
expr_stmt|;
name|provider
operator|.
name|setFormattedOutput
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|provider
operator|.
name|writeTo
argument_list|(
operator|new
name|Book
argument_list|(
literal|"a"
argument_list|)
argument_list|,
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"application/atom+xml;type=entry"
argument_list|)
argument_list|,
literal|null
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|ByteArrayInputStream
name|bis
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|Entry
name|entry
init|=
operator|new
name|AtomEntryProvider
argument_list|()
operator|.
name|readFrom
argument_list|(
name|Entry
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|bis
argument_list|)
decl_stmt|;
name|verifyEntry
argument_list|(
name|entry
argument_list|,
literal|"a"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadEntryWithBuilders
parameter_list|()
throws|throws
name|Exception
block|{
name|AtomPojoProvider
name|provider
init|=
operator|(
name|AtomPojoProvider
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"atom3"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|provider
argument_list|)
expr_stmt|;
name|doTestReadEntry
argument_list|(
name|provider
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadEntryWithoutBuilders
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestReadEntry
argument_list|(
operator|new
name|AtomPojoProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doTestReadEntry
parameter_list|(
name|AtomPojoProvider
name|provider
parameter_list|)
throws|throws
name|Exception
block|{
name|provider
operator|.
name|setFormattedOutput
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|MediaType
name|mt
init|=
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"application/atom+xml;type=entry"
argument_list|)
decl_stmt|;
name|provider
operator|.
name|writeTo
argument_list|(
operator|new
name|Book
argument_list|(
literal|"a"
argument_list|)
argument_list|,
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|mt
argument_list|,
literal|null
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|ByteArrayInputStream
name|bis
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
name|Book
name|book
init|=
operator|(
name|Book
operator|)
name|provider
operator|.
name|readFrom
argument_list|(
operator|(
name|Class
operator|)
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|mt
argument_list|,
literal|null
argument_list|,
name|bis
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|book
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadEntryNoBuilders2
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|entry
init|=
literal|"<!DOCTYPE entry SYSTEM \"entry://entry\"><entry xmlns=\"http://www.w3.org/2005/Atom\">"
operator|+
literal|"<title type=\"text\">a</title>"
operator|+
literal|"<content type=\"application/xml\">"
operator|+
literal|"<book xmlns=\"\">"
operator|+
literal|"<name>a</name>"
operator|+
literal|"</book>"
operator|+
literal|"</content>"
operator|+
literal|"</entry>"
decl_stmt|;
name|AtomPojoProvider
name|provider
init|=
operator|new
name|AtomPojoProvider
argument_list|()
decl_stmt|;
name|ByteArrayInputStream
name|bis
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|entry
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|MediaType
name|mt
init|=
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"application/atom+xml;type=entry"
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
name|Book
name|book
init|=
operator|(
name|Book
operator|)
name|provider
operator|.
name|readFrom
argument_list|(
operator|(
name|Class
operator|)
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|mt
argument_list|,
literal|null
argument_list|,
name|bis
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|book
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadFeedWithBuilders
parameter_list|()
throws|throws
name|Exception
block|{
name|AtomPojoProvider
name|provider
init|=
operator|(
name|AtomPojoProvider
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"atom4"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|provider
argument_list|)
expr_stmt|;
name|doTestReadFeed
argument_list|(
name|provider
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadFeedWithoutBuilders
parameter_list|()
throws|throws
name|Exception
block|{
name|AtomPojoProvider
name|provider
init|=
operator|new
name|AtomPojoProvider
argument_list|()
decl_stmt|;
name|doTestReadFeed
argument_list|(
name|provider
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doTestReadFeed
parameter_list|(
name|AtomPojoProvider
name|provider
parameter_list|)
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|MediaType
name|mt
init|=
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"application/atom+xml;type=feed"
argument_list|)
decl_stmt|;
name|Books
name|books
init|=
operator|new
name|Books
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Book
argument_list|>
name|bs
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|bs
operator|.
name|add
argument_list|(
operator|new
name|Book
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|bs
operator|.
name|add
argument_list|(
operator|new
name|Book
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|books
operator|.
name|setBooks
argument_list|(
name|bs
argument_list|)
expr_stmt|;
name|provider
operator|.
name|writeTo
argument_list|(
name|books
argument_list|,
name|Books
operator|.
name|class
argument_list|,
name|Books
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|mt
argument_list|,
literal|null
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|ByteArrayInputStream
name|bis
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
name|Books
name|books2
init|=
operator|(
name|Books
operator|)
name|provider
operator|.
name|readFrom
argument_list|(
operator|(
name|Class
operator|)
name|Books
operator|.
name|class
argument_list|,
name|Books
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|mt
argument_list|,
literal|null
argument_list|,
name|bis
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Book
argument_list|>
name|list
init|=
name|books2
operator|.
name|getBooks
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"a"
operator|.
name|equals
argument_list|(
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
literal|"a"
operator|.
name|equals
argument_list|(
name|list
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"b"
operator|.
name|equals
argument_list|(
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
literal|"b"
operator|.
name|equals
argument_list|(
name|list
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadFeedWithoutBuilders2
parameter_list|()
throws|throws
name|Exception
block|{
name|AtomPojoProvider
name|provider
init|=
operator|new
name|AtomPojoProvider
argument_list|()
decl_stmt|;
specifier|final
name|String
name|feed
init|=
literal|"<!DOCTYPE feed SYSTEM \"feed://feed\"><feed xmlns=\"http://www.w3.org/2005/Atom\">"
operator|+
literal|"<entry><content type=\"application/xml\"><book xmlns=\"\"><name>a</name></book></content></entry>"
operator|+
literal|"<entry><content type=\"application/xml\"><book xmlns=\"\"><name>b</name></book></content></entry>"
operator|+
literal|"</feed>"
decl_stmt|;
name|MediaType
name|mt
init|=
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"application/atom+xml;type=feed"
argument_list|)
decl_stmt|;
name|ByteArrayInputStream
name|bis
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|feed
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
name|Books
name|books2
init|=
operator|(
name|Books
operator|)
name|provider
operator|.
name|readFrom
argument_list|(
operator|(
name|Class
operator|)
name|Books
operator|.
name|class
argument_list|,
name|Books
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|mt
argument_list|,
literal|null
argument_list|,
name|bis
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Book
argument_list|>
name|list
init|=
name|books2
operator|.
name|getBooks
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"a"
operator|.
name|equals
argument_list|(
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
literal|"a"
operator|.
name|equals
argument_list|(
name|list
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"b"
operator|.
name|equals
argument_list|(
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
literal|"b"
operator|.
name|equals
argument_list|(
name|list
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadEntryNoContent
parameter_list|()
throws|throws
name|Exception
block|{
comment|/** A sample entry without content. */
specifier|final
name|String
name|entryNoContent
init|=
literal|"<?xml version='1.0' encoding='UTF-8'?>\n"
operator|+
literal|"<entry xmlns=\"http://www.w3.org/2005/Atom\">\n"
operator|+
literal|"<id>84297856</id>\n"
operator|+
literal|"</entry>"
decl_stmt|;
name|AtomPojoProvider
name|atomPojoProvider
init|=
operator|new
name|AtomPojoProvider
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
name|JaxbDataType
name|type
init|=
operator|(
name|JaxbDataType
operator|)
name|atomPojoProvider
operator|.
name|readFrom
argument_list|(
operator|(
name|Class
operator|)
name|JaxbDataType
operator|.
name|class
argument_list|,
name|JaxbDataType
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[
literal|0
index|]
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"application/atom+xml;type=entry"
argument_list|)
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|entryNoContent
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadEntryWithUpperCaseTypeParam
parameter_list|()
throws|throws
name|Exception
block|{
name|doReadEntryWithContent
argument_list|(
literal|"application/atom+xml;type=ENTRY"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadEntryNoTypeParam
parameter_list|()
throws|throws
name|Exception
block|{
name|doReadEntryWithContent
argument_list|(
literal|"application/atom+xml"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doReadEntryWithContent
parameter_list|(
name|String
name|mediaType
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|String
name|entryWithContent
init|=
literal|"<?xml version='1.0' encoding='UTF-8'?>\n"
operator|+
literal|"<entry xmlns=\"http://www.w3.org/2005/Atom\">\n"
operator|+
literal|"<id>84297856</id>\n"
operator|+
literal|"<content type=\"application/xml\">\n"
operator|+
literal|"<jaxbDataType xmlns=\"\">\n"
operator|+
literal|"</jaxbDataType>\n"
operator|+
literal|"</content>\n"
operator|+
literal|"</entry>"
decl_stmt|;
name|AtomPojoProvider
name|atomPojoProvider
init|=
operator|new
name|AtomPojoProvider
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
name|JaxbDataType
name|type
init|=
operator|(
name|JaxbDataType
operator|)
name|atomPojoProvider
operator|.
name|readFrom
argument_list|(
operator|(
name|Class
operator|)
name|JaxbDataType
operator|.
name|class
argument_list|,
name|JaxbDataType
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[
literal|0
index|]
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
name|mediaType
argument_list|)
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|entryWithContent
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * A sample JAXB data-type to read data into.      */
annotation|@
name|XmlRootElement
specifier|public
specifier|static
class|class
name|JaxbDataType
block|{
comment|// no data
block|}
specifier|private
name|Entry
name|getEntry
parameter_list|(
name|List
argument_list|<
name|Entry
argument_list|>
name|entries
parameter_list|,
name|String
name|title
parameter_list|)
block|{
for|for
control|(
name|Entry
name|e
range|:
name|entries
control|)
block|{
if|if
condition|(
name|title
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getTitle
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|e
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|verifyEntry
parameter_list|(
name|Entry
name|e
parameter_list|,
name|String
name|title
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|title
argument_list|,
name|e
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
class|class
name|CustomFeedWriter
implements|implements
name|AtomElementWriter
argument_list|<
name|Feed
argument_list|,
name|Books
argument_list|>
block|{
specifier|public
name|void
name|writeTo
parameter_list|(
name|Feed
name|feed
parameter_list|,
name|Books
name|pojoFeed
parameter_list|)
block|{
name|feed
operator|.
name|setTitle
argument_list|(
literal|"Books"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
class|class
name|CustomEntryWriter
implements|implements
name|AtomElementWriter
argument_list|<
name|Entry
argument_list|,
name|Book
argument_list|>
block|{
specifier|public
name|void
name|writeTo
parameter_list|(
name|Entry
name|entry
parameter_list|,
name|Book
name|pojoEntry
parameter_list|)
block|{
name|entry
operator|.
name|setTitle
argument_list|(
name|pojoEntry
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
class|class
name|CustomEntryReader
implements|implements
name|AtomElementReader
argument_list|<
name|Entry
argument_list|,
name|Book
argument_list|>
block|{
specifier|public
name|Book
name|readFrom
parameter_list|(
name|Entry
name|element
parameter_list|)
block|{
try|try
block|{
name|String
name|s
init|=
name|element
operator|.
name|getContent
argument_list|()
decl_stmt|;
name|Unmarshaller
name|um
init|=
operator|new
name|JAXBElementProvider
argument_list|<
name|Book
argument_list|>
argument_list|()
operator|.
name|getJAXBContext
argument_list|(
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|)
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
return|return
operator|(
name|Book
operator|)
name|um
operator|.
name|unmarshal
argument_list|(
operator|new
name|StringReader
argument_list|(
name|s
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
return|return
literal|null
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|CustomFeedReader
implements|implements
name|AtomElementReader
argument_list|<
name|Feed
argument_list|,
name|Books
argument_list|>
block|{
specifier|public
name|Books
name|readFrom
parameter_list|(
name|Feed
name|element
parameter_list|)
block|{
name|Books
name|books
init|=
operator|new
name|Books
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Book
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|CustomEntryReader
name|entryReader
init|=
operator|new
name|CustomEntryReader
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
name|e
range|:
name|element
operator|.
name|getEntries
argument_list|()
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|entryReader
operator|.
name|readFrom
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|books
operator|.
name|setBooks
argument_list|(
name|list
argument_list|)
expr_stmt|;
return|return
name|books
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|CustomFeedBuilder
extends|extends
name|AbstractFeedBuilder
argument_list|<
name|Books
argument_list|>
block|{
annotation|@
name|Override
specifier|public
name|String
name|getBaseUri
parameter_list|(
name|Books
name|books
parameter_list|)
block|{
return|return
literal|"http://books"
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|CustomEntryBuilder
extends|extends
name|AbstractEntryBuilder
argument_list|<
name|Book
argument_list|>
block|{
annotation|@
name|Override
specifier|public
name|String
name|getBaseUri
parameter_list|(
name|Book
name|books
parameter_list|)
block|{
return|return
literal|"http://book"
return|;
block|}
block|}
annotation|@
name|XmlRootElement
specifier|public
specifier|static
class|class
name|Book
block|{
specifier|private
name|String
name|name
init|=
literal|"Book"
decl_stmt|;
specifier|public
name|Book
parameter_list|()
block|{          }
specifier|public
name|Book
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
name|String
name|getXMLContent
parameter_list|()
block|{
return|return
literal|"<"
operator|+
name|name
operator|+
literal|"/>"
return|;
block|}
block|}
annotation|@
name|XmlRootElement
specifier|public
specifier|static
class|class
name|Books
block|{
specifier|private
name|List
argument_list|<
name|Book
argument_list|>
name|books
decl_stmt|;
specifier|public
name|Books
parameter_list|()
block|{          }
specifier|public
name|List
argument_list|<
name|Book
argument_list|>
name|getBooks
parameter_list|()
block|{
return|return
name|books
return|;
block|}
specifier|public
name|void
name|setBooks
parameter_list|(
name|List
argument_list|<
name|Book
argument_list|>
name|list
parameter_list|)
block|{
name|books
operator|=
name|list
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

