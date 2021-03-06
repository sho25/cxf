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
name|jca
operator|.
name|servant
package|;
end_package

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|RemoteException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ejb
operator|.
name|EJBHome
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ejb
operator|.
name|EJBMetaData
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ejb
operator|.
name|Handle
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ejb
operator|.
name|HomeHandle
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ejb
operator|.
name|RemoveException
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

begin_class
specifier|public
class|class
name|ThreadContextCheckerHome
implements|implements
name|EJBHome
block|{
specifier|final
name|Object
name|ejb
decl_stmt|;
specifier|final
name|ClassLoader
name|cl
decl_stmt|;
specifier|final
name|Assert
name|test
decl_stmt|;
specifier|public
name|ThreadContextCheckerHome
parameter_list|(
name|Object
name|ejbObj
parameter_list|,
name|ClassLoader
name|cLoader
parameter_list|,
name|Assert
name|tCase
parameter_list|)
block|{
name|this
operator|.
name|ejb
operator|=
name|ejbObj
expr_stmt|;
name|this
operator|.
name|cl
operator|=
name|cLoader
expr_stmt|;
name|this
operator|.
name|test
operator|=
name|tCase
expr_stmt|;
block|}
specifier|public
name|Object
name|create
parameter_list|()
throws|throws
name|RemoteException
block|{
name|ClassLoader
name|current
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertSame
argument_list|(
literal|"thread context classloader is set as expected, current="
operator|+
name|current
argument_list|,
name|current
argument_list|,
name|cl
argument_list|)
expr_stmt|;
return|return
name|ejb
return|;
block|}
comment|// default impemenations
specifier|public
name|void
name|remove
parameter_list|(
name|Handle
name|handle
parameter_list|)
throws|throws
name|RemoteException
throws|,
name|RemoveException
block|{
comment|// do nothing here
block|}
specifier|public
name|void
name|remove
parameter_list|(
name|Object
name|primaryKey
parameter_list|)
throws|throws
name|RemoteException
throws|,
name|RemoveException
block|{
comment|// do nothing here
block|}
specifier|public
name|EJBMetaData
name|getEJBMetaData
parameter_list|()
throws|throws
name|RemoteException
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|HomeHandle
name|getHomeHandle
parameter_list|()
throws|throws
name|RemoteException
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

