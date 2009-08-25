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
name|configuration
operator|.
name|spring
package|;
end_package

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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|CastUtils
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
name|Test
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

begin_class
specifier|public
class|class
name|SpringBeanQNameMapTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testPersons
parameter_list|()
block|{
name|ClassPathXmlApplicationContext
name|context
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/cxf/configuration/spring/beanQNameMap.xml"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|QName
argument_list|,
name|Person
argument_list|>
name|beans
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
operator|(
name|MapProvider
operator|)
name|context
operator|.
name|getBean
argument_list|(
literal|"committers"
argument_list|)
operator|)
operator|.
name|createMap
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|beans
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|PersonQNameImpl
operator|.
name|getLoadCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|beans
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|QName
name|qn1
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org"
argument_list|,
literal|"anonymous"
argument_list|)
decl_stmt|;
name|Person
name|p1
init|=
name|beans
operator|.
name|get
argument_list|(
name|qn1
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|p1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|PersonQNameImpl
operator|.
name|getLoadCount
argument_list|()
argument_list|)
expr_stmt|;
name|QName
name|qn2
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org"
argument_list|,
literal|"myself"
argument_list|)
decl_stmt|;
name|Person
name|p2
init|=
name|beans
operator|.
name|get
argument_list|(
name|qn2
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|p2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|PersonQNameImpl
operator|.
name|getLoadCount
argument_list|()
argument_list|)
expr_stmt|;
name|QName
name|qn3
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org"
argument_list|,
literal|"other"
argument_list|)
decl_stmt|;
name|Person
name|p3
init|=
name|beans
operator|.
name|get
argument_list|(
name|qn3
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|p3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|PersonQNameImpl
operator|.
name|getLoadCount
argument_list|()
argument_list|)
expr_stmt|;
name|QName
name|qn4
init|=
operator|new
name|QName
argument_list|(
literal|"http://x.y.z"
argument_list|,
literal|"other"
argument_list|)
decl_stmt|;
name|Person
name|p
init|=
name|beans
operator|.
name|get
argument_list|(
name|qn4
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|p3
argument_list|,
name|p
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|PersonQNameImpl
operator|.
name|getLoadCount
argument_list|()
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Person
argument_list|>
name|values
init|=
name|beans
operator|.
name|values
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|values
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Entry
argument_list|<
name|QName
argument_list|,
name|Person
argument_list|>
argument_list|>
name|entries
init|=
name|beans
operator|.
name|entrySet
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|entries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|QName
name|qn5
init|=
operator|new
name|QName
argument_list|(
literal|"http://a.b.c"
argument_list|,
literal|"foo"
argument_list|)
decl_stmt|;
name|Person
name|p4
init|=
operator|new
name|PersonQNameImpl
argument_list|()
decl_stmt|;
name|beans
operator|.
name|put
argument_list|(
name|qn5
argument_list|,
name|p4
argument_list|)
expr_stmt|;
name|p
operator|=
name|beans
operator|.
name|get
argument_list|(
name|qn5
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|p1
argument_list|,
name|beans
operator|.
name|get
argument_list|(
name|qn1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

