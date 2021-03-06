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
name|staxutils
operator|.
name|validation
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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
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
name|Collection
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
name|xml
operator|.
name|stream
operator|.
name|XMLInputFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
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
name|endpoint
operator|.
name|Endpoint
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
name|service
operator|.
name|model
operator|.
name|SchemaInfo
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
name|service
operator|.
name|model
operator|.
name|ServiceInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaCollection
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
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|anyObject
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|anyString
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|expect
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|replay
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|containsString
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|is
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|notNullValue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|nullValue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|MatcherAssert
operator|.
name|assertThat
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|Parameterized
operator|.
name|class
argument_list|)
specifier|public
class|class
name|Stax2ValidationUtilsTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|VALID_MESSAGE_ECHO
init|=
literal|"<echo xmlns=\"http://www.echo.org\">"
operator|+
literal|"<echo>Testing echo</echo>"
operator|+
literal|"</echo>"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|INVALID_MESSAGE_ECHO
init|=
literal|"<wrongEcho xmlns=\"http://www.echo.org\">"
operator|+
literal|"<echo>Testing echo</echo>"
operator|+
literal|"</wrongEcho>"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|VALID_MESSAGE_LOG
init|=
literal|"<log xmlns=\"http://www.log.org\">"
operator|+
literal|"<message>Testing Log</message>"
operator|+
literal|"</log>"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|INVALID_MESSAGE_LOG
init|=
literal|"<wrongLog xmlns=\"http://www.log.org\">"
operator|+
literal|"<message>Testing Log</message>"
operator|+
literal|"</wrongLog>"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ECHO_ERROR_MESSAGE
init|=
literal|"tag name \"wrongEcho\" is not allowed."
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|LOG_ERROR_MESSAGE
init|=
literal|"tag name \"wrongLog\" is not allowed."
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ECHO_SCHEMA
init|=
literal|"schemas/echoSchema.xsd"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|LOG_SCHEMA
init|=
literal|"schemas/logSchema.xsd"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|MULTI_IMPORT_SCHEMA
init|=
literal|"schemas/schemaWithImports.xsd"
decl_stmt|;
specifier|private
name|Stax2ValidationUtils
name|utils
init|=
operator|new
name|Stax2ValidationUtils
argument_list|()
decl_stmt|;
specifier|private
name|XMLStreamReader
name|xmlReader
decl_stmt|;
specifier|private
specifier|final
name|Endpoint
name|endpoint
init|=
name|mock
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|ServiceInfo
name|serviceInfo
init|=
operator|new
name|ServiceInfo
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|SchemaInfo
name|schemaInfo
init|=
operator|new
name|SchemaInfo
argument_list|(
literal|"testUri"
argument_list|)
decl_stmt|;
specifier|private
name|String
name|validMessage
decl_stmt|;
specifier|private
name|String
name|invalidMessage
decl_stmt|;
specifier|private
name|String
name|errorMessage
decl_stmt|;
specifier|private
name|String
name|schemaPath
decl_stmt|;
specifier|public
name|Stax2ValidationUtilsTest
parameter_list|(
name|String
name|validMessage
parameter_list|,
name|String
name|invalidMessage
parameter_list|,
name|String
name|errorMessage
parameter_list|,
name|String
name|schemaPath
parameter_list|)
block|{
name|this
operator|.
name|validMessage
operator|=
name|validMessage
expr_stmt|;
name|this
operator|.
name|invalidMessage
operator|=
name|invalidMessage
expr_stmt|;
name|this
operator|.
name|errorMessage
operator|=
name|errorMessage
expr_stmt|;
name|this
operator|.
name|schemaPath
operator|=
name|schemaPath
expr_stmt|;
block|}
annotation|@
name|Parameterized
operator|.
name|Parameters
specifier|public
specifier|static
name|Collection
argument_list|<
name|String
index|[]
argument_list|>
name|data
parameter_list|()
block|{
name|List
argument_list|<
name|String
index|[]
argument_list|>
name|parameters
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|add
argument_list|(
operator|new
name|String
index|[]
block|{
name|VALID_MESSAGE_ECHO
block|,
name|INVALID_MESSAGE_ECHO
block|,
name|ECHO_ERROR_MESSAGE
block|,
name|MULTI_IMPORT_SCHEMA
block|}
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|add
argument_list|(
operator|new
name|String
index|[]
block|{
name|VALID_MESSAGE_LOG
block|,
name|INVALID_MESSAGE_LOG
block|,
name|LOG_ERROR_MESSAGE
block|,
name|MULTI_IMPORT_SCHEMA
block|}
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|add
argument_list|(
operator|new
name|String
index|[]
block|{
name|VALID_MESSAGE_ECHO
block|,
name|INVALID_MESSAGE_ECHO
block|,
name|ECHO_ERROR_MESSAGE
block|,
name|ECHO_SCHEMA
block|}
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|add
argument_list|(
operator|new
name|String
index|[]
block|{
name|VALID_MESSAGE_LOG
block|,
name|INVALID_MESSAGE_LOG
block|,
name|LOG_ERROR_MESSAGE
block|,
name|LOG_SCHEMA
block|}
argument_list|)
expr_stmt|;
return|return
name|parameters
return|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlSchemaCollection
name|schemaCol
init|=
operator|new
name|XmlSchemaCollection
argument_list|()
decl_stmt|;
name|InputStream
name|io
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|schemaPath
argument_list|)
decl_stmt|;
name|String
name|sysId
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
name|schemaPath
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|schemaCol
operator|.
name|setBaseUri
argument_list|(
name|getTestBaseURI
argument_list|()
argument_list|)
expr_stmt|;
name|schemaCol
operator|.
name|read
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|io
argument_list|,
name|sysId
argument_list|)
argument_list|)
expr_stmt|;
name|serviceInfo
operator|.
name|addSchema
argument_list|(
name|schemaInfo
argument_list|)
expr_stmt|;
name|schemaInfo
operator|.
name|setSchema
argument_list|(
name|schemaCol
operator|.
name|getXmlSchema
argument_list|(
name|sysId
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|endpoint
operator|.
name|get
argument_list|(
name|anyObject
argument_list|()
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|endpoint
operator|.
name|containsKey
argument_list|(
name|anyObject
argument_list|()
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|endpoint
operator|.
name|put
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|anyObject
argument_list|()
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testValidMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|Throwable
name|exception
init|=
literal|null
decl_stmt|;
name|xmlReader
operator|=
name|createReader
argument_list|(
name|validMessage
argument_list|)
expr_stmt|;
name|utils
operator|.
name|setupValidation
argument_list|(
name|xmlReader
argument_list|,
name|endpoint
argument_list|,
name|serviceInfo
argument_list|)
expr_stmt|;
try|try
block|{
while|while
condition|(
name|xmlReader
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|xmlReader
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|exception
operator|=
name|e
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|exception
argument_list|,
name|is
argument_list|(
name|nullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvalidMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|Throwable
name|exception
init|=
literal|null
decl_stmt|;
name|xmlReader
operator|=
name|createReader
argument_list|(
name|invalidMessage
argument_list|)
expr_stmt|;
name|utils
operator|.
name|setupValidation
argument_list|(
name|xmlReader
argument_list|,
name|endpoint
argument_list|,
name|serviceInfo
argument_list|)
expr_stmt|;
try|try
block|{
while|while
condition|(
name|xmlReader
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|xmlReader
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|exception
operator|=
name|e
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|exception
argument_list|,
name|is
argument_list|(
name|notNullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|exception
operator|.
name|getMessage
argument_list|()
argument_list|,
name|containsString
argument_list|(
name|errorMessage
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getTestBaseURI
parameter_list|()
block|{
name|ClassLoader
name|classLoader
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|classLoader
operator|.
name|getResource
argument_list|(
name|schemaPath
argument_list|)
operator|.
name|getFile
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|file
operator|.
name|getAbsolutePath
argument_list|()
return|;
block|}
specifier|private
name|XMLStreamReader
name|createReader
parameter_list|(
name|String
name|message
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|Reader
name|reader
init|=
operator|new
name|StringReader
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|XMLInputFactory
name|factory
init|=
name|XMLInputFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
return|return
name|factory
operator|.
name|createXMLStreamReader
argument_list|(
name|reader
argument_list|)
return|;
block|}
block|}
end_class

end_unit

