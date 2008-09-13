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
name|transport
operator|.
name|https
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Socket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|UnknownHostException
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
name|Handler
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
name|Level
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
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLSocket
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLSocketFactory
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

begin_class
class|class
name|SSLSocketFactoryWrapper
extends|extends
name|SSLSocketFactory
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
name|SSLSocketFactoryWrapper
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|SSLSocketFactory
name|sslSocketFactory
decl_stmt|;
specifier|private
name|String
index|[]
name|ciphers
decl_stmt|;
specifier|private
name|String
name|protocol
decl_stmt|;
specifier|public
name|SSLSocketFactoryWrapper
parameter_list|(
name|SSLSocketFactory
name|sslSocketFactoryParam
parameter_list|,
name|String
index|[]
name|ciphersParam
parameter_list|,
name|String
name|protocolParam
parameter_list|)
block|{
name|sslSocketFactory
operator|=
name|sslSocketFactoryParam
expr_stmt|;
name|ciphers
operator|=
name|ciphersParam
expr_stmt|;
name|protocol
operator|=
name|protocolParam
expr_stmt|;
block|}
specifier|public
name|String
index|[]
name|getDefaultCipherSuites
parameter_list|()
block|{
return|return
name|sslSocketFactory
operator|.
name|getDefaultCipherSuites
argument_list|()
return|;
block|}
specifier|public
name|String
index|[]
name|getSupportedCipherSuites
parameter_list|()
block|{
return|return
name|sslSocketFactory
operator|.
name|getSupportedCipherSuites
argument_list|()
return|;
block|}
specifier|public
name|Socket
name|createSocket
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|enableCipherSuites
argument_list|(
name|sslSocketFactory
operator|.
name|createSocket
argument_list|()
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"unconnected"
block|,
literal|"unconnected"
block|}
argument_list|)
return|;
block|}
specifier|public
name|Socket
name|createSocket
parameter_list|(
name|Socket
name|s
parameter_list|,
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|,
name|boolean
name|autoClose
parameter_list|)
throws|throws
name|IOException
throws|,
name|UnknownHostException
block|{
return|return
name|enableCipherSuites
argument_list|(
name|sslSocketFactory
operator|.
name|createSocket
argument_list|(
name|s
argument_list|,
name|host
argument_list|,
name|port
argument_list|,
name|autoClose
argument_list|)
argument_list|,
operator|new
name|Object
index|[]
block|{
name|host
block|,
name|port
block|}
argument_list|)
return|;
block|}
specifier|public
name|Socket
name|createSocket
parameter_list|(
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|)
throws|throws
name|IOException
throws|,
name|UnknownHostException
block|{
return|return
name|enableCipherSuites
argument_list|(
name|sslSocketFactory
operator|.
name|createSocket
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
argument_list|,
operator|new
name|Object
index|[]
block|{
name|host
block|,
name|port
block|}
argument_list|)
return|;
block|}
specifier|public
name|Socket
name|createSocket
parameter_list|(
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|,
name|InetAddress
name|localHost
parameter_list|,
name|int
name|localPort
parameter_list|)
throws|throws
name|IOException
throws|,
name|UnknownHostException
block|{
return|return
name|enableCipherSuites
argument_list|(
name|sslSocketFactory
operator|.
name|createSocket
argument_list|(
name|host
argument_list|,
name|port
argument_list|,
name|localHost
argument_list|,
name|localPort
argument_list|)
argument_list|,
operator|new
name|Object
index|[]
block|{
name|host
block|,
name|port
block|}
argument_list|)
return|;
block|}
specifier|public
name|Socket
name|createSocket
parameter_list|(
name|InetAddress
name|host
parameter_list|,
name|int
name|port
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|enableCipherSuites
argument_list|(
name|sslSocketFactory
operator|.
name|createSocket
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
argument_list|,
operator|new
name|Object
index|[]
block|{
name|host
block|,
name|port
block|}
argument_list|)
return|;
block|}
specifier|public
name|Socket
name|createSocket
parameter_list|(
name|InetAddress
name|address
parameter_list|,
name|int
name|port
parameter_list|,
name|InetAddress
name|localAddress
parameter_list|,
name|int
name|localPort
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|enableCipherSuites
argument_list|(
name|sslSocketFactory
operator|.
name|createSocket
argument_list|(
name|address
argument_list|,
name|port
argument_list|,
name|localAddress
argument_list|,
name|localPort
argument_list|)
argument_list|,
operator|new
name|Object
index|[]
block|{
name|address
block|,
name|port
block|}
argument_list|)
return|;
block|}
specifier|private
name|Socket
name|enableCipherSuites
parameter_list|(
name|Socket
name|s
parameter_list|,
name|Object
index|[]
name|logParams
parameter_list|)
block|{
name|SSLSocket
name|socket
init|=
operator|(
name|SSLSocket
operator|)
name|s
decl_stmt|;
if|if
condition|(
operator|(
name|socket
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|ciphers
operator|!=
literal|null
operator|)
condition|)
block|{
name|socket
operator|.
name|setEnabledCipherSuites
argument_list|(
name|ciphers
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|socket
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|protocol
operator|!=
literal|null
operator|)
condition|)
block|{
name|socket
operator|.
name|setEnabledProtocols
argument_list|(
operator|new
name|String
index|[]
block|{
name|protocol
block|}
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|socket
operator|==
literal|null
condition|)
block|{
name|LogUtils
operator|.
name|log
argument_list|(
name|LOG
argument_list|,
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"PROBLEM_CREATING_OUTBOUND_REQUEST_SOCKET"
argument_list|,
name|logParams
argument_list|)
expr_stmt|;
block|}
return|return
name|socket
return|;
block|}
comment|/*      * For testing only      */
specifier|protected
name|void
name|addLogHandler
parameter_list|(
name|Handler
name|handler
parameter_list|)
block|{
name|LOG
operator|.
name|addHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

