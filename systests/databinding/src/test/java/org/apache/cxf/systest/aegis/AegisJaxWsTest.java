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
name|aegis
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
name|Arrays
import|;
end_import

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
name|List
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|aegis
operator|.
name|databinding
operator|.
name|AegisDatabinding
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
name|FileUtils
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
name|JaxWsProxyFactoryBean
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
name|systest
operator|.
name|aegis
operator|.
name|bean
operator|.
name|Item
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
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|security
operator|.
name|wss4j
operator|.
name|WSS4JOutInterceptor
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
name|ContextConfiguration
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
name|AbstractJUnit4SpringContextTests
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

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|ContextConfiguration
argument_list|(
name|locations
operator|=
block|{
literal|"classpath:aegisJaxWsBeans.xml"
block|}
argument_list|)
specifier|public
class|class
name|AegisJaxWsTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
specifier|static
specifier|final
name|String
name|PORT
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|AegisJaxWsTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|AegisJaxWs
name|client
decl_stmt|;
specifier|public
name|AegisJaxWsTest
parameter_list|()
block|{     }
specifier|private
name|void
name|setupForTest
parameter_list|(
name|boolean
name|sec
parameter_list|)
throws|throws
name|Exception
block|{
name|JaxWsProxyFactoryBean
name|factory
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|AegisJaxWs
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|sec
condition|)
block|{
name|factory
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/aegisJaxWsUN"
argument_list|)
expr_stmt|;
name|WSS4JOutInterceptor
name|wss4jOut
init|=
operator|new
name|WSS4JOutInterceptor
argument_list|()
decl_stmt|;
name|wss4jOut
operator|.
name|setProperty
argument_list|(
literal|"action"
argument_list|,
literal|"UsernameToken"
argument_list|)
expr_stmt|;
name|wss4jOut
operator|.
name|setProperty
argument_list|(
literal|"user"
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|wss4jOut
operator|.
name|setProperty
argument_list|(
literal|"password"
argument_list|,
literal|"pass"
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setProperties
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
literal|"password"
argument_list|,
literal|"pass"
argument_list|)
expr_stmt|;
name|factory
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|wss4jOut
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|factory
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/aegisJaxWs"
argument_list|)
expr_stmt|;
block|}
name|factory
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setDataBinding
argument_list|(
operator|new
name|AegisDatabinding
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|=
operator|(
name|AegisJaxWs
operator|)
name|factory
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetItemSecure
parameter_list|()
throws|throws
name|Exception
block|{
name|setupForTest
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Item
name|item
init|=
name|client
operator|.
name|getItemByKey
argument_list|(
literal|"   jack&jill   "
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|33
argument_list|,
name|item
operator|.
name|getKey
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"   jack&jill   :b"
argument_list|,
name|item
operator|.
name|getData
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetItem
parameter_list|()
throws|throws
name|Exception
block|{
name|setupForTest
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Item
name|item
init|=
name|client
operator|.
name|getItemByKey
argument_list|(
literal|" a "
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|33
argument_list|,
name|item
operator|.
name|getKey
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|" a :b"
argument_list|,
name|item
operator|.
name|getData
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMapSpecified
parameter_list|()
throws|throws
name|Exception
block|{
name|setupForTest
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Item
name|item
init|=
operator|new
name|Item
argument_list|()
decl_stmt|;
name|item
operator|.
name|setKey
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|42
argument_list|)
argument_list|)
expr_stmt|;
name|item
operator|.
name|setData
argument_list|(
literal|"Godzilla"
argument_list|)
expr_stmt|;
name|client
operator|.
name|addItem
argument_list|(
name|item
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|Item
argument_list|>
name|items
init|=
name|client
operator|.
name|getItemsMapSpecified
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|items
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|items
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Map
operator|.
name|Entry
argument_list|<
name|Integer
argument_list|,
name|Item
argument_list|>
name|entry
init|=
name|items
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|entry
argument_list|)
expr_stmt|;
name|Item
name|item2
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|Integer
name|key2
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|42
argument_list|,
name|key2
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Godzilla"
argument_list|,
name|item2
operator|.
name|getData
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetStringList
parameter_list|()
throws|throws
name|Exception
block|{
name|setupForTest
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Integer
name|soucet
init|=
name|client
operator|.
name|getSimpleValue
argument_list|(
literal|5
argument_list|,
literal|"aa"
argument_list|)
decl_stmt|;
comment|//this one fail, when comment org.apache.cxf.systest.aegis.AegisJaxWs.getStringList test pass
name|Assert
operator|.
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|5
argument_list|)
argument_list|,
name|soucet
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|item
init|=
name|client
operator|.
name|getStringList
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|)
argument_list|,
name|item
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBigList
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|size
init|=
literal|1000
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|l
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|size
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|size
condition|;
name|x
operator|++
control|)
block|{
name|l
operator|.
name|add
argument_list|(
literal|"Need to create a pretty big soap message to make sure we go over "
operator|+
literal|"some of the default buffer sizes and such so we can see what really"
operator|+
literal|"happens when we do that - "
operator|+
name|x
argument_list|)
expr_stmt|;
block|}
name|setupForTest
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|item
init|=
name|client
operator|.
name|echoBigList
argument_list|(
name|l
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|size
argument_list|,
name|item
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|//CXF-2768
name|File
name|f
init|=
name|FileUtils
operator|.
name|getDefaultTempDir
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|f
operator|.
name|listFiles
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
comment|//CXF-3376
specifier|public
name|void
name|testByteArray
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|size
init|=
literal|50
decl_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|ints
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|size
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|size
condition|;
name|x
operator|++
control|)
block|{
name|ints
operator|.
name|add
argument_list|(
name|x
argument_list|)
expr_stmt|;
block|}
name|setupForTest
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bytes
init|=
name|client
operator|.
name|export
argument_list|(
name|ints
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|bytes
operator|.
name|length
operator|>
literal|50
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

