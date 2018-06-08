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
name|corba
operator|.
name|utils
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|corba
operator|.
name|CorbaBindingException
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
name|omg
operator|.
name|CORBA
operator|.
name|ORB
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|CorbaBindingHelper
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|CorbaBindingHelper
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|ORB
argument_list|>
name|orbList
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|orbUseCount
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|static
name|ORB
name|defaultORB
decl_stmt|;
specifier|private
name|CorbaBindingHelper
parameter_list|()
block|{
comment|//utility class
block|}
specifier|public
specifier|static
specifier|synchronized
name|ORB
name|getDefaultORB
parameter_list|(
name|OrbConfig
name|config
parameter_list|)
block|{
if|if
condition|(
name|defaultORB
operator|==
literal|null
condition|)
block|{
name|Properties
name|props
init|=
name|System
operator|.
name|getProperties
argument_list|()
decl_stmt|;
name|Properties
name|configSpecifiedOrbProperties
init|=
name|config
operator|.
name|getOrbProperties
argument_list|()
decl_stmt|;
name|props
operator|.
name|putAll
argument_list|(
name|configSpecifiedOrbProperties
argument_list|)
expr_stmt|;
if|if
condition|(
name|config
operator|.
name|getOrbClass
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
literal|"org.omg.CORBA.ORBClass"
argument_list|,
name|config
operator|.
name|getOrbClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getOrbSingletonClass
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
literal|"org.omg.CORBA.ORBSingletonClass"
argument_list|,
name|config
operator|.
name|getOrbSingletonClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|orbArgs
init|=
name|config
operator|.
name|getOrbArgs
argument_list|()
decl_stmt|;
name|defaultORB
operator|=
name|ORB
operator|.
name|init
argument_list|(
name|orbArgs
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
argument_list|,
name|props
argument_list|)
expr_stmt|;
if|if
condition|(
name|defaultORB
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|severe
argument_list|(
literal|"Could not create instance of the ORB"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"Could not create instance of the ORB"
argument_list|)
throw|;
block|}
block|}
return|return
name|defaultORB
return|;
block|}
specifier|public
specifier|static
specifier|synchronized
name|ORB
name|getAddressSpecificORB
parameter_list|(
name|String
name|address
parameter_list|,
name|Properties
name|props
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|orbArgs
parameter_list|)
block|{
name|ORB
name|orb
init|=
name|orbList
operator|.
name|get
argument_list|(
name|getORBNameFromAddress
argument_list|(
name|address
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|orb
operator|==
literal|null
condition|)
block|{
name|orb
operator|=
name|createAddressSpecificORB
argument_list|(
name|address
argument_list|,
name|props
argument_list|,
name|orbArgs
argument_list|)
expr_stmt|;
block|}
return|return
name|orb
return|;
block|}
specifier|private
specifier|static
name|ORB
name|createAddressSpecificORB
parameter_list|(
name|String
name|address
parameter_list|,
name|Properties
name|props
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|orbArgs
parameter_list|)
block|{
name|ORB
name|orb
init|=
literal|null
decl_stmt|;
name|URI
name|addressURI
init|=
literal|null
decl_stmt|;
try|try
block|{
name|addressURI
operator|=
operator|new
name|URI
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"Unable to create ORB with address "
operator|+
name|address
argument_list|)
throw|;
block|}
name|String
name|scheme
init|=
name|addressURI
operator|.
name|getScheme
argument_list|()
decl_stmt|;
comment|// A corbaloc address gives us host and port information to use when setting up the
comment|// endpoint for the ORB.  Other types of references will just create ORBs on the
comment|// host and port used when no preference has been specified.
if|if
condition|(
literal|"corbaloc"
operator|.
name|equals
argument_list|(
name|scheme
argument_list|)
condition|)
block|{
name|String
name|schemeSpecificPart
init|=
name|addressURI
operator|.
name|getSchemeSpecificPart
argument_list|()
decl_stmt|;
name|int
name|keyIndex
init|=
name|schemeSpecificPart
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
name|String
name|corbaAddr
init|=
name|schemeSpecificPart
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|keyIndex
argument_list|)
decl_stmt|;
name|int
name|index
init|=
name|corbaAddr
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
name|String
name|protocol
init|=
literal|"iiop"
decl_stmt|;
if|if
condition|(
name|index
operator|!=
literal|0
condition|)
block|{
name|protocol
operator|=
name|corbaAddr
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
name|int
name|oldIndex
init|=
name|index
decl_stmt|;
name|index
operator|=
name|corbaAddr
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|,
name|oldIndex
operator|+
literal|1
argument_list|)
expr_stmt|;
name|String
name|host
init|=
name|corbaAddr
operator|.
name|substring
argument_list|(
name|oldIndex
operator|+
literal|1
argument_list|,
name|index
argument_list|)
decl_stmt|;
name|String
name|port
init|=
name|corbaAddr
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"yoko.orb.oa.endpoint"
argument_list|,
operator|new
name|String
argument_list|(
name|protocol
operator|+
literal|" --host "
operator|+
name|host
operator|+
literal|" --port "
operator|+
name|port
argument_list|)
argument_list|)
expr_stmt|;
comment|// WHAT to do for non-yoko orb?
block|}
elseif|else
if|if
condition|(
literal|"corbaname"
operator|.
name|equals
argument_list|(
name|scheme
argument_list|)
condition|)
block|{
name|String
name|schemeSpecificPart
init|=
name|addressURI
operator|.
name|getSchemeSpecificPart
argument_list|()
decl_stmt|;
if|if
condition|(
name|schemeSpecificPart
operator|.
name|startsWith
argument_list|(
literal|":"
argument_list|)
condition|)
block|{
name|schemeSpecificPart
operator|=
name|schemeSpecificPart
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|int
name|idx
init|=
name|schemeSpecificPart
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"org.omg.CORBA.ORBInitialHost"
argument_list|,
name|schemeSpecificPart
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"org.omg.CORBA.ORBInitialPort"
argument_list|,
name|schemeSpecificPart
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"file"
operator|.
name|equals
argument_list|(
name|scheme
argument_list|)
operator|||
literal|"relfile"
operator|.
name|equals
argument_list|(
name|scheme
argument_list|)
operator|||
literal|"IOR"
operator|.
name|equals
argument_list|(
name|scheme
argument_list|)
operator|||
literal|"ior"
operator|.
name|equals
argument_list|(
name|scheme
argument_list|)
condition|)
block|{
comment|//use defaults
block|}
else|else
block|{
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"Unsupported address scheme type "
operator|+
name|scheme
argument_list|)
throw|;
block|}
name|orb
operator|=
name|ORB
operator|.
name|init
argument_list|(
name|orbArgs
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
argument_list|,
name|props
argument_list|)
expr_stmt|;
name|orbList
operator|.
name|put
argument_list|(
name|getORBNameFromAddress
argument_list|(
name|address
argument_list|)
argument_list|,
name|orb
argument_list|)
expr_stmt|;
return|return
name|orb
return|;
block|}
specifier|private
specifier|static
name|String
name|getORBNameFromAddress
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|String
name|name
init|=
literal|null
decl_stmt|;
name|URI
name|addressURI
init|=
literal|null
decl_stmt|;
try|try
block|{
name|addressURI
operator|=
operator|new
name|URI
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"Unable to locate ORB with address "
operator|+
name|address
argument_list|)
throw|;
block|}
name|String
name|scheme
init|=
name|addressURI
operator|.
name|getScheme
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"corbaloc"
operator|.
name|equals
argument_list|(
name|scheme
argument_list|)
operator|||
literal|"corbaname"
operator|.
name|equals
argument_list|(
name|scheme
argument_list|)
condition|)
block|{
name|String
name|schemeSpecificPart
init|=
name|addressURI
operator|.
name|getSchemeSpecificPart
argument_list|()
decl_stmt|;
if|if
condition|(
name|schemeSpecificPart
operator|.
name|startsWith
argument_list|(
literal|":"
argument_list|)
condition|)
block|{
name|schemeSpecificPart
operator|=
name|schemeSpecificPart
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|int
name|keyIndex
init|=
name|schemeSpecificPart
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|keyIndex
operator|!=
operator|-
literal|1
condition|)
block|{
name|name
operator|=
name|schemeSpecificPart
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|keyIndex
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|name
operator|=
name|schemeSpecificPart
expr_stmt|;
block|}
if|if
condition|(
name|addressURI
operator|.
name|getRawQuery
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|name
operator|+=
name|addressURI
operator|.
name|getRawQuery
argument_list|()
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"IOR"
operator|.
name|equals
argument_list|(
name|scheme
argument_list|)
operator|||
literal|"ior"
operator|.
name|equals
argument_list|(
name|scheme
argument_list|)
condition|)
block|{
name|name
operator|=
name|addressURI
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"file"
operator|.
name|equals
argument_list|(
name|scheme
argument_list|)
operator|||
literal|"relfile"
operator|.
name|equals
argument_list|(
name|scheme
argument_list|)
condition|)
block|{
name|name
operator|=
name|addressURI
operator|.
name|getPath
argument_list|()
expr_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|name
operator|=
name|addressURI
operator|.
name|getSchemeSpecificPart
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"Unsupported address scheme type "
operator|+
name|scheme
argument_list|)
throw|;
block|}
return|return
name|name
return|;
block|}
comment|// This indicates that we need to keep the ORB alive.  This allows multiple objects to share the
comment|// same ORB and not have one of the objects destroy it while other objects are using it.
specifier|public
specifier|static
specifier|synchronized
name|void
name|keepORBAlive
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|Integer
name|count
init|=
name|orbUseCount
operator|.
name|get
argument_list|(
name|getORBNameFromAddress
argument_list|(
name|address
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|count
operator|==
literal|null
condition|)
block|{
name|orbUseCount
operator|.
name|put
argument_list|(
name|getORBNameFromAddress
argument_list|(
name|address
argument_list|)
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|orbUseCount
operator|.
name|put
argument_list|(
name|getORBNameFromAddress
argument_list|(
name|address
argument_list|)
argument_list|,
name|count
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Signals that the ORB should be tested to see if it can be destroyed.  Actual destruction will
comment|// only occur if the ORB is not being used by someone else.  If it is, then we simply decrement
comment|// the count.
specifier|public
specifier|static
specifier|synchronized
name|void
name|destroyORB
parameter_list|(
name|String
name|address
parameter_list|,
name|ORB
name|orb
parameter_list|)
throws|throws
name|CorbaBindingException
block|{
name|Integer
name|count
init|=
name|orbUseCount
operator|.
name|get
argument_list|(
name|getORBNameFromAddress
argument_list|(
name|address
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|count
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|count
operator|=
name|count
operator|-
literal|1
expr_stmt|;
if|if
condition|(
name|count
operator|<
literal|1
condition|)
block|{
comment|// We shouldn't have anyone waiting on this ORB.  Destroy it.
name|orbUseCount
operator|.
name|remove
argument_list|(
name|getORBNameFromAddress
argument_list|(
name|address
argument_list|)
argument_list|)
expr_stmt|;
name|orbList
operator|.
name|remove
argument_list|(
name|getORBNameFromAddress
argument_list|(
name|address
argument_list|)
argument_list|)
expr_stmt|;
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
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|orbUseCount
operator|.
name|put
argument_list|(
name|getORBNameFromAddress
argument_list|(
name|address
argument_list|)
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

