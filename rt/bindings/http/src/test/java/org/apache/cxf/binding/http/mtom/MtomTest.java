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
name|binding
operator|.
name|http
operator|.
name|mtom
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
name|javax
operator|.
name|activation
operator|.
name|DataHandler
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|BindingFactoryManager
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
name|binding
operator|.
name|http
operator|.
name|AbstractRestTest
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
name|binding
operator|.
name|http
operator|.
name|HttpBindingFactory
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
name|ServerImpl
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
name|jaxws
operator|.
name|JaxWsServerFactoryBean
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
name|person
operator|.
name|Person
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
name|invoker
operator|.
name|BeanInvoker
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
name|MtomTest
extends|extends
name|AbstractRestTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testService
parameter_list|()
throws|throws
name|Exception
block|{
name|BindingFactoryManager
name|bfm
init|=
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|HttpBindingFactory
name|factory
init|=
operator|new
name|HttpBindingFactory
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|bfm
operator|.
name|registerBindingFactory
argument_list|(
name|HttpBindingFactory
operator|.
name|HTTP_BINDING_ID
argument_list|,
name|factory
argument_list|)
expr_stmt|;
name|PeopleServiceImpl
name|impl
init|=
operator|new
name|PeopleServiceImpl
argument_list|()
decl_stmt|;
name|JaxWsServerFactoryBean
name|sf
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setBindingId
argument_list|(
name|HttpBindingFactory
operator|.
name|HTTP_BINDING_ID
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setServiceBean
argument_list|(
name|impl
argument_list|)
expr_stmt|;
name|sf
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setInvoker
argument_list|(
operator|new
name|BeanInvoker
argument_list|(
name|impl
argument_list|)
argument_list|)
expr_stmt|;
name|sf
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setWrapped
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:9001/"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"mtom-enabled"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setProperties
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|ServerImpl
name|svr
init|=
operator|(
name|ServerImpl
operator|)
name|sf
operator|.
name|create
argument_list|()
decl_stmt|;
comment|// TEST POST/GETs
name|Person
name|p
init|=
operator|new
name|Person
argument_list|()
decl_stmt|;
name|p
operator|.
name|setName
argument_list|(
literal|"Dan"
argument_list|)
expr_stmt|;
name|DataHandler
name|handler
init|=
operator|new
name|DataHandler
argument_list|(
operator|new
name|ByteArrayDataSource
argument_list|(
literal|"foo"
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|"application/octet-stream"
argument_list|)
argument_list|)
decl_stmt|;
name|p
operator|.
name|setPhoto
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|impl
operator|.
name|addPerson
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|byte
index|[]
name|res
init|=
name|doMethodBytes
argument_list|(
literal|"http://localhost:9001/people"
argument_list|,
literal|null
argument_list|,
literal|"GET"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|res
argument_list|)
expr_stmt|;
comment|// TODO: Test response
comment|// IOUtils.copy(new ByteArrayInputStream(res), System.out);
name|String
name|ct
init|=
literal|"multipart/related; type=\"application/xop+xml\"; "
operator|+
literal|"start=\"<addPerson.xml>\"; "
operator|+
literal|"start-info=\"text/xml; charset=utf-8\"; "
operator|+
literal|"boundary=\"----=_Part_4_701508.1145579811786\""
decl_stmt|;
name|res
operator|=
name|doMethodBytes
argument_list|(
literal|"http://localhost:9001/people"
argument_list|,
literal|"addPerson"
argument_list|,
literal|"POST"
argument_list|,
name|ct
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|res
argument_list|)
expr_stmt|;
comment|// TODO: Test response
comment|// IOUtils.copy(new ByteArrayInputStream(res), System.out);
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|impl
operator|.
name|getPeople
argument_list|()
operator|.
name|getPerson
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|svr
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

