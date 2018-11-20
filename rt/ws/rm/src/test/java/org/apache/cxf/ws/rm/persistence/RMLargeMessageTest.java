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
name|ws
operator|.
name|rm
operator|.
name|persistence
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
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

begin_comment
comment|/**  * A version of RMMessageTest using a cached out set up.  */
end_comment

begin_class
specifier|public
class|class
name|RMLargeMessageTest
extends|extends
name|RMMessageTest
block|{
specifier|private
specifier|static
name|String
name|oldThreshold
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|setupThreshold
parameter_list|()
throws|throws
name|Exception
block|{
name|oldThreshold
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"org.apache.cxf.io.CachedOutputStream.Threshold"
argument_list|)
expr_stmt|;
comment|// forces the CacheOutputStream to use temporary file caching
name|System
operator|.
name|setProperty
argument_list|(
literal|"org.apache.cxf.io.CachedOutputStream.Threshold"
argument_list|,
literal|"16"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|cleanup
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|oldThreshold
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|clearProperty
argument_list|(
literal|"org.apache.cxf.io.CachedOutputStream.Threshold"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"org.apache.cxf.io.CachedOutputStream.Threshold"
argument_list|,
name|oldThreshold
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

