begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|corba
operator|.
name|server
package|;
end_package

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
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|ORB
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|UserException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CosNaming
operator|.
name|NameComponent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CosNaming
operator|.
name|NamingContextExt
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CosNaming
operator|.
name|NamingContextExtHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|PortableServer
operator|.
name|POA
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|PortableServer
operator|.
name|POAHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|PortableServer
operator|.
name|POAManager
import|;
end_import

begin_import
import|import
name|corba
operator|.
name|common
operator|.
name|BankHelper
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|Server
block|{
specifier|private
name|Server
parameter_list|()
block|{
comment|//not created
block|}
specifier|static
name|int
name|run
parameter_list|(
name|ORB
name|orb
parameter_list|,
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|UserException
block|{
comment|//
comment|// Resolve Root POA
comment|//
name|POA
name|poa
init|=
name|POAHelper
operator|.
name|narrow
argument_list|(
name|orb
operator|.
name|resolve_initial_references
argument_list|(
literal|"RootPOA"
argument_list|)
argument_list|)
decl_stmt|;
comment|//
comment|// Get a reference to the POA manager
comment|//
name|POAManager
name|manager
init|=
name|poa
operator|.
name|the_POAManager
argument_list|()
decl_stmt|;
comment|//
comment|// Create implementation object
comment|//
name|BankImpl
name|bankImpl
init|=
operator|new
name|BankImpl
argument_list|(
name|poa
argument_list|)
decl_stmt|;
name|byte
index|[]
name|oid
init|=
literal|"Bank"
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|poa
operator|.
name|activate_object_with_id
argument_list|(
name|oid
argument_list|,
name|bankImpl
argument_list|)
expr_stmt|;
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|Object
name|ref
init|=
name|poa
operator|.
name|create_reference_with_id
argument_list|(
name|oid
argument_list|,
name|BankHelper
operator|.
name|id
argument_list|()
argument_list|)
decl_stmt|;
comment|// Register in NameService
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|Object
name|nsObj
init|=
name|orb
operator|.
name|resolve_initial_references
argument_list|(
literal|"NameService"
argument_list|)
decl_stmt|;
name|NamingContextExt
name|rootContext
init|=
name|NamingContextExtHelper
operator|.
name|narrow
argument_list|(
name|nsObj
argument_list|)
decl_stmt|;
name|NameComponent
index|[]
name|nc
init|=
name|rootContext
operator|.
name|to_name
argument_list|(
literal|"Bank"
argument_list|)
decl_stmt|;
name|rootContext
operator|.
name|rebind
argument_list|(
name|nc
argument_list|,
name|ref
argument_list|)
expr_stmt|;
comment|//
comment|// Run implementation
comment|//
name|manager
operator|.
name|activate
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Server ready..."
argument_list|)
expr_stmt|;
name|orb
operator|.
name|run
argument_list|()
expr_stmt|;
return|return
literal|0
return|;
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
name|args
index|[]
parameter_list|)
block|{
name|Properties
name|props
init|=
name|System
operator|.
name|getProperties
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"org.omg.CORBA.ORBInitialHost"
argument_list|,
literal|"localhost"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"org.omg.CORBA.ORBInitialPort"
argument_list|,
literal|"1050"
argument_list|)
expr_stmt|;
name|int
name|status
init|=
literal|0
decl_stmt|;
name|ORB
name|orb
init|=
literal|null
decl_stmt|;
try|try
block|{
name|orb
operator|=
name|ORB
operator|.
name|init
argument_list|(
name|args
argument_list|,
name|props
argument_list|)
expr_stmt|;
name|status
operator|=
name|run
argument_list|(
name|orb
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|status
operator|=
literal|1
expr_stmt|;
block|}
if|if
condition|(
name|orb
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|orb
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|status
operator|=
literal|1
expr_stmt|;
block|}
block|}
name|System
operator|.
name|exit
argument_list|(
name|status
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

