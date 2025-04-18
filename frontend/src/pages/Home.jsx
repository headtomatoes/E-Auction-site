import React from 'react';
import Header from '../components/Header';
import MainBanner from '../components/MainBanner';
import PopularCategories from '../components/PopularCategories';
import MoneyBackBanner from '../components/MoneyBackBanner';
import Footer from '../components/Footer';
import { useAuth } from '../contexts/AuthContext';

export default function Home() {
  // Access authentication state and user info from context
  const { isAuthenticated, user, hasRole } = useAuth();

  return (
    <>
      <Header />

      //Greeting
      <div className="max-w-6xl mx-auto px-4 py-8">
        <div className="text-center mb-6">
          {isAuthenticated ? (
            <h1 className="text-2xl font-semibold">
              Hi, <span className="text-blue-600">{user.username}</span>! ({user.roles.join(', ')})
            </h1>
          ) : (
            <h1 className="text-2xl font-semibold">Welcome to our store!</h1>
          )}
        </div>

        //Role-based content
        {isAuthenticated && (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {hasRole('ROLE_ADMIN') && (
              <div className="bg-purple-50 border border-purple-200 rounded-lg p-6">
                <h2 className="text-xl font-semibold mb-4 text-purple-800">Admin Panel</h2>
                <p className="mb-4">You have administrative privileges.</p>
                <ul className="list-disc list-inside text-sm text-purple-900 text-left">
                  <li>Manage users and permissions</li>
                  <li>Review system reports</li>
                  <li>Moderate product listings</li>
                </ul>
              </div>
            )}

            {hasRole('ROLE_SELLER') && (
              <div className="bg-blue-50 border border-blue-200 rounded-lg p-6">
                <h2 className="text-xl font-semibold mb-4 text-blue-800">Seller Tools</h2>
                <p className="mb-4">You can list and manage products.</p>
                <ul className="list-disc list-inside text-sm text-blue-900 text-left">
                  <li>Create new product listings</li>
                  <li>Update prices and descriptions</li>
                  <li>Track and manage your orders</li>
                </ul>
              </div>
            )}

            {hasRole('ROLE_BUYER') && (
              <div className="bg-green-50 border border-green-200 rounded-lg p-6">
                <h2 className="text-xl font-semibold mb-4 text-green-800">Buyer Portal</h2>
                <p className="mb-4">You can browse and purchase products.</p>
                <ul className="list-disc list-inside text-sm text-green-900 text-left">
                  <li>Search and view product listings</li>
                  <li>Add items to your cart</li>
                  <li>Track your purchase history</li>
                </ul>
              </div>
            )}
          </div>
        )}
      </div>

      <MainBanner />
      <PopularCategories />
      <MoneyBackBanner />
      <Footer />
    </>
  );
}
