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
name|provider
operator|.
name|datasource
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
name|net
operator|.
name|HttpURLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
import|import
name|javax
operator|.
name|activation
operator|.
name|DataSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|MessagingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|internet
operator|.
name|MimeMessage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|internet
operator|.
name|MimeMultipart
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|util
operator|.
name|ByteArrayDataSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|OutputKeys
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Transformer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamResult
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusClientServerTestBase
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
name|TestUtil
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
name|BeforeClass
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
name|DataSourceProviderTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|static
name|String
name|serverPort
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|Server
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getLogger
argument_list|(
name|DataSourceProviderTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|BOUNDARY
init|=
literal|"----=_Part_4_701508.1145579811786"
decl_stmt|;
specifier|private
name|HttpURLConnection
name|conn
decl_stmt|;
specifier|private
name|URL
name|url
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|startServers
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|Server
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|createConnection
parameter_list|()
throws|throws
name|Exception
block|{
name|url
operator|=
operator|new
name|URL
argument_list|(
literal|"http://localhost:"
operator|+
name|serverPort
operator|+
literal|"/test/foo"
argument_list|)
expr_stmt|;
name|conn
operator|=
operator|(
name|HttpURLConnection
operator|)
name|url
operator|.
name|openConnection
argument_list|()
expr_stmt|;
name|conn
operator|.
name|setDoOutput
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|invokeOnServer
parameter_list|()
throws|throws
name|Exception
block|{
name|url
operator|=
operator|new
name|URL
argument_list|(
literal|"http://localhost:"
operator|+
name|serverPort
operator|+
literal|"/test/foo"
argument_list|)
expr_stmt|;
name|conn
operator|=
operator|(
name|HttpURLConnection
operator|)
name|url
operator|.
name|openConnection
argument_list|()
expr_stmt|;
name|printSource
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|conn
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|postAttachmentToServer
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|contentType
init|=
literal|"multipart/related; type=\"text/xml\"; "
operator|+
literal|"start=\"attachmentData\"; "
operator|+
literal|"boundary=\""
operator|+
name|BOUNDARY
operator|+
literal|"\""
decl_stmt|;
name|InputStream
name|in
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/attachmentBinaryData"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"could not load test data"
argument_list|,
name|in
argument_list|)
expr_stmt|;
name|conn
operator|.
name|setRequestMethod
argument_list|(
literal|"POST"
argument_list|)
expr_stmt|;
name|conn
operator|.
name|addRequestProperty
argument_list|(
literal|"Content-Type"
argument_list|,
name|contentType
argument_list|)
expr_stmt|;
name|OutputStream
name|out
init|=
name|conn
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|MimeMultipart
name|mm
init|=
name|readAttachmentParts
argument_list|(
name|conn
operator|.
name|getContentType
argument_list|()
argument_list|,
name|conn
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"incorrect number of parts received by server"
argument_list|,
literal|3
argument_list|,
name|mm
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|printSource
parameter_list|(
name|Source
name|source
parameter_list|)
block|{
try|try
block|{
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|StreamResult
name|sr
init|=
operator|new
name|StreamResult
argument_list|(
name|bos
argument_list|)
decl_stmt|;
name|Transformer
name|trans
init|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newTransformer
argument_list|()
decl_stmt|;
name|Properties
name|oprops
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|oprops
operator|.
name|put
argument_list|(
name|OutputKeys
operator|.
name|OMIT_XML_DECLARATION
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|trans
operator|.
name|setOutputProperties
argument_list|(
name|oprops
argument_list|)
expr_stmt|;
name|trans
operator|.
name|transform
argument_list|(
name|source
argument_list|,
name|sr
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|bos
operator|.
name|toString
argument_list|()
argument_list|,
literal|"<doc><response>Hello</response></doc>"
argument_list|)
expr_stmt|;
name|bos
operator|.
name|close
argument_list|()
expr_stmt|;
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
specifier|public
specifier|static
name|MimeMultipart
name|readAttachmentParts
parameter_list|(
name|String
name|contentType
parameter_list|,
name|InputStream
name|bais
parameter_list|)
throws|throws
name|MessagingException
throws|,
name|IOException
block|{
name|DataSource
name|source
init|=
operator|new
name|ByteArrayDataSource
argument_list|(
name|bais
argument_list|,
name|contentType
argument_list|)
decl_stmt|;
name|MimeMultipart
name|mpart
init|=
operator|new
name|MimeMultipart
argument_list|(
name|source
argument_list|)
decl_stmt|;
name|Session
name|session
init|=
name|Session
operator|.
name|getDefaultInstance
argument_list|(
operator|new
name|Properties
argument_list|()
argument_list|)
decl_stmt|;
name|MimeMessage
name|mm
init|=
operator|new
name|MimeMessage
argument_list|(
name|session
argument_list|)
decl_stmt|;
name|mm
operator|.
name|setContent
argument_list|(
name|mpart
argument_list|)
expr_stmt|;
name|mm
operator|.
name|addHeaderLine
argument_list|(
literal|"Content-Type:"
operator|+
name|contentType
argument_list|)
expr_stmt|;
return|return
operator|(
name|MimeMultipart
operator|)
name|mm
operator|.
name|getContent
argument_list|()
return|;
block|}
block|}
end_class

end_unit

