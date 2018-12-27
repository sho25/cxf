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
name|jaxws
operator|.
name|service
package|;
end_package

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
name|jws
operator|.
name|WebService
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
name|Server
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
name|feature
operator|.
name|Feature
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
name|feature
operator|.
name|Features
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
name|frontend
operator|.
name|ServerFactoryBean
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
name|interceptor
operator|.
name|InFaultInterceptors
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
name|interceptor
operator|.
name|InInterceptors
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
name|interceptor
operator|.
name|Interceptor
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
name|interceptor
operator|.
name|OutFaultInterceptors
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
name|AbstractJaxWsTest
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
name|jaxws
operator|.
name|service
operator|.
name|AnnotationFeature
operator|.
name|AnnotationFeatureInterceptor
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
name|Message
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
name|AnnotationInterceptorTest
extends|extends
name|AbstractJaxWsTest
block|{
specifier|private
name|ServerFactoryBean
name|fb
decl_stmt|;
specifier|private
name|Server
name|server
decl_stmt|;
specifier|private
name|JaxWsServerFactoryBean
name|jfb
decl_stmt|;
specifier|private
name|Server
name|jserver
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|fb
operator|=
operator|new
name|ServerFactoryBean
argument_list|()
expr_stmt|;
name|fb
operator|.
name|setAddress
argument_list|(
literal|"local://localhost"
argument_list|)
expr_stmt|;
name|fb
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|jfb
operator|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
expr_stmt|;
name|jfb
operator|.
name|setAddress
argument_list|(
literal|"local://localhost"
argument_list|)
expr_stmt|;
name|jfb
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSimpleFrontend
parameter_list|()
throws|throws
name|Exception
block|{
name|fb
operator|.
name|setServiceClass
argument_list|(
name|HelloService
operator|.
name|class
argument_list|)
expr_stmt|;
name|HelloService
name|hello
init|=
operator|new
name|HelloServiceImpl
argument_list|()
decl_stmt|;
name|fb
operator|.
name|setServiceBean
argument_list|(
name|hello
argument_list|)
expr_stmt|;
name|server
operator|=
name|fb
operator|.
name|create
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|interceptors
init|=
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|hasTestInterceptor
argument_list|(
name|interceptors
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|hasTest2Interceptor
argument_list|(
name|interceptors
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|outFaultInterceptors
init|=
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getOutFaultInterceptors
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|hasTestInterceptor
argument_list|(
name|outFaultInterceptors
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|hasTest2Interceptor
argument_list|(
name|outFaultInterceptors
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSimpleFrontendWithFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|fb
operator|.
name|setServiceClass
argument_list|(
name|HelloService
operator|.
name|class
argument_list|)
expr_stmt|;
name|HelloService
name|hello
init|=
operator|new
name|HelloServiceImpl
argument_list|()
decl_stmt|;
name|fb
operator|.
name|setServiceBean
argument_list|(
name|hello
argument_list|)
expr_stmt|;
name|server
operator|=
name|fb
operator|.
name|create
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Feature
argument_list|>
name|features
init|=
name|fb
operator|.
name|getFeatures
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|hasAnnotationFeature
argument_list|(
name|features
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSimpleFrontendWithNoAnnotation
parameter_list|()
throws|throws
name|Exception
block|{
name|fb
operator|.
name|setServiceClass
argument_list|(
name|HelloService
operator|.
name|class
argument_list|)
expr_stmt|;
name|HelloService
name|hello
init|=
operator|new
name|HelloServiceImplNoAnnotation
argument_list|()
decl_stmt|;
name|fb
operator|.
name|setServiceBean
argument_list|(
name|hello
argument_list|)
expr_stmt|;
name|server
operator|=
name|fb
operator|.
name|create
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|interceptors
init|=
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|hasTestInterceptor
argument_list|(
name|interceptors
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Feature
argument_list|>
name|features
init|=
name|fb
operator|.
name|getFeatures
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|hasAnnotationFeature
argument_list|(
name|features
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJaxwsFrontendWithNoAnnotation
parameter_list|()
throws|throws
name|Exception
block|{
name|jfb
operator|.
name|setServiceClass
argument_list|(
name|SayHi
operator|.
name|class
argument_list|)
expr_stmt|;
name|jfb
operator|.
name|setServiceBean
argument_list|(
operator|new
name|SayHiNoInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|jserver
operator|=
name|jfb
operator|.
name|create
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|interceptors
init|=
name|jserver
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|hasTestInterceptor
argument_list|(
name|interceptors
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Feature
argument_list|>
name|features
init|=
name|fb
operator|.
name|getFeatures
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|hasAnnotationFeature
argument_list|(
name|features
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJaxwsFrontendWithAnnotationInImpl
parameter_list|()
throws|throws
name|Exception
block|{
name|jfb
operator|.
name|setServiceClass
argument_list|(
name|SayHi
operator|.
name|class
argument_list|)
expr_stmt|;
name|SayHi
name|implementor
init|=
operator|new
name|SayHiImplementation
argument_list|()
decl_stmt|;
name|jfb
operator|.
name|setServiceBean
argument_list|(
name|implementor
argument_list|)
expr_stmt|;
name|jserver
operator|=
name|jfb
operator|.
name|create
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|interceptors
init|=
name|jserver
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|hasTestInterceptor
argument_list|(
name|interceptors
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|inFaultInterceptors
init|=
name|jserver
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getInFaultInterceptors
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|hasTestInterceptor
argument_list|(
name|inFaultInterceptors
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|hasTest2Interceptor
argument_list|(
name|inFaultInterceptors
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Feature
argument_list|>
name|features
init|=
name|jfb
operator|.
name|getFeatures
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|hasAnnotationFeature
argument_list|(
name|features
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJaxwsFrontendWithFeatureAnnotation
parameter_list|()
throws|throws
name|Exception
block|{
name|jfb
operator|.
name|setServiceClass
argument_list|(
name|SayHi
operator|.
name|class
argument_list|)
expr_stmt|;
name|SayHi
name|implementor
init|=
operator|new
name|SayHiImplementation
argument_list|()
decl_stmt|;
name|jfb
operator|.
name|setServiceBean
argument_list|(
name|implementor
argument_list|)
expr_stmt|;
name|jserver
operator|=
name|jfb
operator|.
name|create
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|interceptors
init|=
name|jserver
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|hasAnnotationFeatureInterceptor
argument_list|(
name|interceptors
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|outInterceptors
init|=
name|jserver
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getOutInterceptors
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|hasAnnotationFeatureInterceptor
argument_list|(
name|outInterceptors
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJaxWsFrontendWithAnnotationInSEI
parameter_list|()
throws|throws
name|Exception
block|{
name|jfb
operator|.
name|setServiceClass
argument_list|(
name|SayHiInterfaceImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|jfb
operator|.
name|setServiceBean
argument_list|(
operator|new
name|SayHiInterfaceImpl
argument_list|()
argument_list|)
expr_stmt|;
name|jserver
operator|=
name|jfb
operator|.
name|create
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|interceptors
init|=
name|jserver
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|hasTestInterceptor
argument_list|(
name|interceptors
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Feature
argument_list|>
name|features
init|=
name|jfb
operator|.
name|getFeatures
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|hasAnnotationFeature
argument_list|(
name|features
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJaxWsFrontendWithAnnotationInSEIAndImpl
parameter_list|()
throws|throws
name|Exception
block|{
name|jfb
operator|.
name|setServiceClass
argument_list|(
name|SayHiInterface
operator|.
name|class
argument_list|)
expr_stmt|;
name|jfb
operator|.
name|setServiceBean
argument_list|(
operator|new
name|SayHiInterfaceImpl2
argument_list|()
argument_list|)
expr_stmt|;
name|jserver
operator|=
name|jfb
operator|.
name|create
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|interceptors
init|=
name|jserver
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|hasTestInterceptor
argument_list|(
name|interceptors
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|hasTest2Interceptor
argument_list|(
name|interceptors
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|hasTestInterceptor
parameter_list|(
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|interceptors
parameter_list|)
block|{
name|boolean
name|flag
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
name|it
range|:
name|interceptors
control|)
block|{
if|if
condition|(
name|it
operator|instanceof
name|TestInterceptor
condition|)
block|{
name|flag
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|flag
return|;
block|}
specifier|private
name|boolean
name|hasTest2Interceptor
parameter_list|(
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|interceptors
parameter_list|)
block|{
name|boolean
name|flag
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
name|it
range|:
name|interceptors
control|)
block|{
if|if
condition|(
name|it
operator|instanceof
name|Test2Interceptor
condition|)
block|{
name|flag
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|flag
return|;
block|}
specifier|private
name|boolean
name|hasAnnotationFeature
parameter_list|(
name|List
argument_list|<
name|Feature
argument_list|>
name|features
parameter_list|)
block|{
name|boolean
name|flag
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Feature
name|af
range|:
name|features
control|)
block|{
if|if
condition|(
name|af
operator|instanceof
name|AnnotationFeature
condition|)
block|{
name|flag
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|flag
return|;
block|}
specifier|private
name|boolean
name|hasAnnotationFeatureInterceptor
parameter_list|(
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|interceptors
parameter_list|)
block|{
name|boolean
name|flag
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
name|it
range|:
name|interceptors
control|)
block|{
if|if
condition|(
name|it
operator|instanceof
name|AnnotationFeatureInterceptor
condition|)
block|{
name|flag
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|flag
return|;
block|}
annotation|@
name|InInterceptors
argument_list|(
name|classes
operator|=
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|service
operator|.
name|TestInterceptor
operator|.
name|class
argument_list|)
annotation|@
name|OutFaultInterceptors
argument_list|(
name|classes
operator|=
block|{
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|service
operator|.
name|TestInterceptor
operator|.
name|class
block|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|service
operator|.
name|Test2Interceptor
operator|.
name|class
block|}
argument_list|)
annotation|@
name|Features
argument_list|(
name|classes
operator|=
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|service
operator|.
name|AnnotationFeature
operator|.
name|class
argument_list|)
specifier|public
class|class
name|HelloServiceImpl
implements|implements
name|HelloService
block|{
specifier|public
name|String
name|sayHi
parameter_list|()
block|{
return|return
literal|"HI"
return|;
block|}
block|}
specifier|public
class|class
name|HelloServiceImplNoAnnotation
implements|implements
name|HelloService
block|{
specifier|public
name|String
name|sayHi
parameter_list|()
block|{
return|return
literal|"HI"
return|;
block|}
block|}
annotation|@
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"SayHiService"
argument_list|,
name|portName
operator|=
literal|"HelloPort"
argument_list|,
name|targetNamespace
operator|=
literal|"http://mynamespace.com/"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.cxf.jaxws.service.SayHi"
argument_list|)
annotation|@
name|InInterceptors
argument_list|(
name|interceptors
operator|=
block|{
literal|"org.apache.cxf.jaxws.service.TestInterceptor"
block|}
argument_list|)
annotation|@
name|InFaultInterceptors
argument_list|(
name|interceptors
operator|=
block|{
literal|"org.apache.cxf.jaxws.service.Test2Interceptor"
block|}
argument_list|)
annotation|@
name|Features
argument_list|(
name|features
operator|=
literal|"org.apache.cxf.jaxws.service.AnnotationFeature"
argument_list|)
specifier|public
class|class
name|SayHiImplementation
implements|implements
name|SayHi
block|{
specifier|public
name|long
name|sayHi
parameter_list|(
name|long
name|arg
parameter_list|)
block|{
return|return
name|arg
return|;
block|}
specifier|public
name|void
name|greetMe
parameter_list|()
block|{          }
specifier|public
name|String
index|[]
name|getStringArray
parameter_list|(
name|String
index|[]
name|strs
parameter_list|)
block|{
name|String
index|[]
name|strings
init|=
operator|new
name|String
index|[
literal|2
index|]
decl_stmt|;
name|strings
index|[
literal|0
index|]
operator|=
literal|"Hello"
operator|+
name|strs
index|[
literal|0
index|]
expr_stmt|;
name|strings
index|[
literal|1
index|]
operator|=
literal|"Bonjour"
operator|+
name|strs
index|[
literal|1
index|]
expr_stmt|;
return|return
name|strings
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getStringList
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|list
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|ret
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ret
operator|.
name|add
argument_list|(
literal|"Hello"
operator|+
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
literal|"Bonjour"
operator|+
name|list
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
block|}
annotation|@
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"SayHiService"
argument_list|,
name|portName
operator|=
literal|"HelloPort"
argument_list|,
name|targetNamespace
operator|=
literal|"http://mynamespace.com/"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.cxf.jaxws.service.SayHi"
argument_list|)
specifier|public
class|class
name|SayHiNoInterceptor
implements|implements
name|SayHi
block|{
specifier|public
name|long
name|sayHi
parameter_list|(
name|long
name|arg
parameter_list|)
block|{
return|return
name|arg
return|;
block|}
specifier|public
name|void
name|greetMe
parameter_list|()
block|{          }
specifier|public
name|String
index|[]
name|getStringArray
parameter_list|(
name|String
index|[]
name|strs
parameter_list|)
block|{
name|String
index|[]
name|strings
init|=
operator|new
name|String
index|[
literal|2
index|]
decl_stmt|;
name|strings
index|[
literal|0
index|]
operator|=
literal|"Hello"
operator|+
name|strs
index|[
literal|0
index|]
expr_stmt|;
name|strings
index|[
literal|1
index|]
operator|=
literal|"Bonjour"
operator|+
name|strs
index|[
literal|1
index|]
expr_stmt|;
return|return
name|strings
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getStringList
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|list
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|ret
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ret
operator|.
name|add
argument_list|(
literal|"Hello"
operator|+
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
literal|"Bonjour"
operator|+
name|list
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
block|}
annotation|@
name|WebService
argument_list|(
name|endpointInterface
operator|=
literal|"org.apache.cxf.jaxws.service.SayHiInterface"
argument_list|)
specifier|public
class|class
name|SayHiInterfaceImpl
implements|implements
name|SayHiInterface
block|{
specifier|public
name|String
name|sayHi
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
literal|"HI"
return|;
block|}
block|}
annotation|@
name|WebService
argument_list|(
name|endpointInterface
operator|=
literal|"org.apache.cxf.jaxws.service.SayHiInterface"
argument_list|)
annotation|@
name|InInterceptors
argument_list|(
name|classes
operator|=
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|service
operator|.
name|Test2Interceptor
operator|.
name|class
argument_list|)
specifier|public
class|class
name|SayHiInterfaceImpl2
implements|implements
name|SayHiInterface
block|{
specifier|public
name|String
name|sayHi
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
literal|"HI"
return|;
block|}
block|}
block|}
end_class

end_unit

