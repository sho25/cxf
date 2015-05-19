begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2012-2014 the original author or authors.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|sample
operator|.
name|ws
operator|.
name|service
package|;
end_package

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
name|Rule
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
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Value
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|test
operator|.
name|IntegrationTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|test
operator|.
name|OutputCapture
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|test
operator|.
name|SpringApplicationConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|SpringJUnit4ClassRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|web
operator|.
name|WebAppConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|client
operator|.
name|core
operator|.
name|WebServiceTemplate
import|;
end_import

begin_import
import|import
name|sample
operator|.
name|ws
operator|.
name|SampleWsApplication
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|containsString
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
name|assertThat
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|SpringJUnit4ClassRunner
operator|.
name|class
argument_list|)
annotation|@
name|SpringApplicationConfiguration
argument_list|(
name|classes
operator|=
name|SampleWsApplication
operator|.
name|class
argument_list|)
annotation|@
name|WebAppConfiguration
annotation|@
name|IntegrationTest
argument_list|(
literal|"server.port=0"
argument_list|)
specifier|public
class|class
name|SampleWsApplicationTests
block|{
annotation|@
name|Rule
specifier|public
name|OutputCapture
name|output
init|=
operator|new
name|OutputCapture
argument_list|()
decl_stmt|;
specifier|private
name|WebServiceTemplate
name|webServiceTemplate
init|=
operator|new
name|WebServiceTemplate
argument_list|()
decl_stmt|;
annotation|@
name|Value
argument_list|(
literal|"${local.server.port}"
argument_list|)
specifier|private
name|int
name|serverPort
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|this
operator|.
name|webServiceTemplate
operator|.
name|setDefaultUri
argument_list|(
literal|"http://localhost:"
operator|+
name|this
operator|.
name|serverPort
operator|+
literal|"/Service/Hello"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHelloRequest
parameter_list|()
block|{
comment|//final String request = "<q0:sayHello xmlns:q0=\"http://service.ws.sample\">Elan</q0:sayHello>";
specifier|final
name|String
name|request
init|=
literal|"<q0:sayHello xmlns:q0=\"http://service.ws.sample/\"><myname>Elan</myname></q0:sayHello>"
decl_stmt|;
name|StreamSource
name|source
init|=
operator|new
name|StreamSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|request
argument_list|)
argument_list|)
decl_stmt|;
name|StreamResult
name|result
init|=
operator|new
name|StreamResult
argument_list|(
name|System
operator|.
name|out
argument_list|)
decl_stmt|;
name|this
operator|.
name|webServiceTemplate
operator|.
name|sendSourceAndReceiveToResult
argument_list|(
name|source
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|this
operator|.
name|output
operator|.
name|toString
argument_list|()
argument_list|,
name|containsString
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><ns2:sayHelloResponse xmlns:ns2=\"http://service.ws.sample/\"><return>Hello, Welcome to CXF Spring boot Elan!!!</return></ns2:sayHelloResponse>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

